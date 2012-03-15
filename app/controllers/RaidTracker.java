package controllers;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import models.raidtracker.Raid;
import models.raidtracker.RaidBossKills;
import models.raidtracker.RaidItem;
import models.raidtracker.RaidMember;
import models.raidtracker.RaidType;
import models.raidtracker.RaidPool;
import models.raidtracker.RaidZones;
import models.raidtracker.helpers.RaidPoolHelper;
import models.raidtracker.helpers.SortPoolByItems;
import models.raidtracker.helpers.SortPoolByRaidTeilnahme;
import models.wowapi.Armory;
import models.wowapi.character.Character;
import models.wowapi.resources.Item;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import play.Play;
import play.db.DB;
import play.libs.XPath;
import play.mvc.Before;
import play.mvc.Controller;
import flexjson.JSONSerializer;

public class RaidTracker extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
		RaidTracker.setupDefaultData();
	}
	public static void showRaid(Long id) {
		render();
	}
	public static void index(Long selpool) {
		List<RaidPool> pools = RaidPool.findAll();
		RaidPool selectedPool = null;

		if (selpool == null) {
			selpool = 1L;
		}

		selectedPool = RaidPool.findById(selpool);
		List<RaidItem> items = RaidItem.find("raid.pool = ? order by time desc", selectedPool).fetch();

		for (RaidItem raidItem : items) {
			raidItem.checkItem();
		}
		List<Raid> raids = Raid.find("pool = ? order by startDate desc", selectedPool).fetch();
		List<RaidPoolHelper> members = RaidPoolHelper.getRaidPool(selpool, raids);
		List<SortPoolByItems> poolByItems = new ArrayList<SortPoolByItems>();
		List<SortPoolByRaidTeilnahme> poolByRaidTeilnahme = new ArrayList<SortPoolByRaidTeilnahme>();

		for (RaidPoolHelper raidPoolHelper : members) {
			poolByItems.add(new SortPoolByItems(raidPoolHelper));
			poolByRaidTeilnahme.add(new SortPoolByRaidTeilnahme(raidPoolHelper));
		}

		Collections.sort(poolByItems);
		Collections.sort(poolByRaidTeilnahme);

		render(pools, selpool, items, selectedPool, members, raids, poolByItems, poolByRaidTeilnahme);
	}

	public static void showItem(Long id) {
		Item item = Item.findById(id);
		render(item);
	}

	public static void showRaidItem(Long id) {
		RaidItem raiditem = RaidItem.findById(id);
		List<RaidItem> items = RaidItem.find("itemId = ?", raiditem.itemId).fetch();
		render(raiditem, items);
	}

	public static void itemsAsJSON(Long selpool) {
		List<RaidItem> items;
		if (selpool != null) {
			items = RaidItem.find("raid.pool = ? order by time desc", RaidPool.findById(selpool)).fetch(30);
		} else {
			items = RaidItem.all().fetch();
		}
		JSONSerializer characterSerializer = new JSONSerializer().include("formatedDate", "name", "memberName").exclude("*").prettyPrint(true).rootName("aaData");
		renderJSON(characterSerializer.serialize(items));
	}

	public static void character(String name) throws SQLException {
		List<RaidItem> items = new ArrayList<RaidItem>();
		RaidMember member = RaidMember.find("name = ?", name).first();
		List<RaidItem> mitems = RaidItem.find("order by raid desc").fetch();

		List<Raid> raids = new ArrayList<Raid>();
		PreparedStatement ps = DB.getConnection().prepareStatement("select r.id raidId from Raid r join RaidMember rm on (r.id = rm.raid_id) where rm.name = ? order by r.id desc");
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			raids.add((Raid)Raid.findById(rs.getLong("raidId")));
		}
		
		for (RaidItem item : mitems) {
			if (member.name.equals(item.member.name)) {
				items.add(item);
			}
		}
		render(member, items, raids);
	}

	public static void addRaid(Long raidpool, String raidxml) {
		Document xmlDoc = getXMLDocument(raidxml);
		Node gameinfo = XPath.selectNode("//gameinfo", xmlDoc);
		String offizier = XPath.selectText("charactername", gameinfo);
		Character character = Character.find("name =? and realm.name = ?", offizier, Play.configuration.getProperty("wowapi.realmName")).first();
		if (character == null) {
			character = Armory.fetchCharacter(Play.configuration.getProperty("wowapi.realmName"), offizier);
		}
		Date startDate = null;
		Date endDate = null;
		startDate = getStartDate(xmlDoc, startDate);
		endDate = getEndDate(xmlDoc, endDate);
		Long poolId = raidpool;

		Raid raid = Raid.find("startDate = ? and endDate = ? and pool = ?", startDate, endDate, RaidPool.findById(poolId)).first();
		if (raid == null) {

			raid = new Raid(poolId, startDate, endDate, character);
			raid.save();

			new RaidMember("Bank", startDate, endDate, character, raid).save();
			new RaidMember("Entzaubert", startDate, endDate, character, raid).save();

			for (Node zones : XPath.selectNodes("//zones", xmlDoc)) {
				for (Node zone : XPath.selectNodes("//zone", zones)) {
					String enter = XPath.selectText("enter", zone);
					String leave = XPath.selectText("leave", zone);
					String name = XPath.selectText("name", zone);
					String difficulty = XPath.selectText("difficulty", zone);
					RaidType modus = RaidType.find("modus = ?", Long.parseLong(difficulty)).first();
					RaidZones rz = new RaidZones(new Date(Long.parseLong(enter) * 1000), new Date(Long.parseLong(leave) * 1000), name, modus, raid);
					rz.save();
				}
			}

			for (Node bosse : XPath.selectNodes("//bosskills", xmlDoc)) {
				for (Node boss : XPath.selectNodes("//bosskill", bosse)) {
					String name = XPath.selectText("name", boss);
					String date = XPath.selectText("time", boss);
					String difficulty = XPath.selectText("difficulty", boss);
					RaidType modus = RaidType.find("modus = ?", Long.parseLong(difficulty)).first();
					RaidBossKills rbk = new RaidBossKills(name, new Date(Long.parseLong(date) * 1000), modus, raid);
					rbk.save();
				}
			}

			for (Node member : XPath.selectNodes("/raidlog/raiddata/members/member", xmlDoc)) {
				String name = XPath.selectText("name", member);

				Date join = new Date(Long.parseLong(XPath.selectText("times/time[@type='join']", member)) * 1000);
				Date leave = new Date(Long.parseLong(XPath.selectText("times/time[@type='leave']", member)) * 1000);
				Character c = Character.find("name =? and realm.name = ?", name, Play.configuration.getProperty("wowapi.realmName")).first();
				if (c == null) {
					c = Armory.fetchCharacter(Play.configuration.getProperty("wowapi.realmName"), offizier);
				}

				RaidMember rm = new RaidMember(name, join, leave, c, raid);
				rm.save();
			}

			for (Node items : XPath.selectNodes("/raidlog/raiddata/items/item", xmlDoc)) {
				String name = XPath.selectText("name", items);
				Date time = new Date(Long.parseLong(XPath.selectText("time", items)) * 1000);
				String member = XPath.selectText("member", items);

				if (member.equals("bank")) {
					member = "Bank";
				}
				if (member.equals("disenchanted")) {
					member = "Entzaubert";
				}
				RaidMember rm = RaidMember.find("name = ? and raid =?", member, raid).first();

				String sitemId = XPath.selectText("itemid", items);
				Long itemId = Long.parseLong(sitemId.substring(0, sitemId.indexOf(":")));
				Long cost = Long.parseLong(XPath.selectText("cost", items));
				RaidBossKills boss = RaidBossKills.find("name = ? and raid = ?", XPath.selectText("boss", items), raid).first();

				RaidItem ri = new RaidItem(name, time, rm, itemId, cost, boss, raid, Item.setItem(itemId));
				ri.save();
			}
		}
		
		redirect("RaidTracker.index",raidpool);

	}

	private static Document getXMLDocument(String raidxml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Document xmlDoc = null;
		try {
			xmlDoc = builder.parse(new InputSource(new StringReader(raidxml)));

		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return xmlDoc;
	}

	private static Document getTestXMLDocument() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Document xmlDoc = null;
		try {
			xmlDoc = builder.parse(new File("./public/raid1.xml"));
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return xmlDoc;
	}

	private static Date getEndDate(Document xmlDoc, Date endDate) {
		for (Node zones : XPath.selectNodes("//zone", xmlDoc)) {
			String dates = XPath.selectText("leave", zones);
			endDate = new Date(Long.parseLong(dates) * 1000);
		}
		return endDate;
	}

	private static Date getStartDate(Document xmlDoc, Date startDate) {
		for (Node zones : XPath.selectNodes("//zone[1]", xmlDoc)) {
			String dates = XPath.selectText("enter", zones);
			startDate = new Date(Long.parseLong(dates) * 1000);
		}
		return startDate;
	}

	private static void setupDefaultData() {

		if (RaidType.all().fetch().size() != 2) {
			new RaidType(new Long(3), "Heroisch").save();
			new RaidType(new Long(1), "Normal").save();
		}
		if (RaidPool.all().fetch().size() < 2) {
			new RaidPool("Haupt Raid", true).save();
			new RaidPool("Twink Raid", false).save();
		}

	}

}
