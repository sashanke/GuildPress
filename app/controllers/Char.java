package controllers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.raidtracker.Raid;
import models.raidtracker.RaidItem;
import models.raidtracker.RaidMember;
import models.wowapi.character.Avatar;
import models.wowapi.character.AvatarItem;
import models.wowapi.resources.Item;
import play.db.DB;
import play.mvc.Before;
import play.mvc.Controller;

public class Char extends Controller {

	@Before
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void show(Long id, String name, String realm) throws IOException, SQLException {

		if (id == 0L) {
			Avatar avatar = Avatar.createAvatar(name, realm);
			show(avatar.id, name, realm);
		}

		// String banner = Play.configuration.getProperty("conf.bannerdir") +
		// this.race.side.name.toLowerCase() + "/" +
		// this.race.name.toLowerCase() + "/" + this.race.name.toLowerCase() +
		// "_" + Tools.replaceUmlauts(this.cclass.name.toLowerCase()) + "_" +
		// Tools.replaceUmlauts(this.gender.name_loc.toLowerCase()) + ".jpg";

		// List<CharacterClass> cr = CharacterClass.findAll();
		// for (CharacterClass characterRace : cr) {
		// System.out.println("public static final int "+characterRace.name.toUpperCase()+" = "+characterRace.ccId+";");
		// }

		// RaceClassMap.createMap();

		// List<RaceClassMap> rcm = RaceClassMap.findAll();
		// for (RaceClassMap raceClassMap : rcm) {
		// for (CharacterClass cclass : raceClassMap.cclass) {
		// File one = new File("." +
		// Play.configuration.getProperty("conf.bannerdir") +
		// raceClassMap.side.name.toLowerCase() + "/" +
		// raceClassMap.race.name.toLowerCase() + "/" +
		// raceClassMap.race.name.toLowerCase() + "_" +
		// Tools.replaceUmlauts(cclass.name.toLowerCase()) + "_" +
		// Tools.replaceUmlauts("männlich") + ".jpg");
		// File two = new File("." +
		// Play.configuration.getProperty("conf.bannerdir") +
		// raceClassMap.side.name.toLowerCase() + "/" +
		// raceClassMap.race.name.toLowerCase() + "/" +
		// raceClassMap.race.name.toLowerCase() + "_" +
		// Tools.replaceUmlauts(cclass.name.toLowerCase()) + "_" +
		// Tools.replaceUmlauts("weiblich") + ".jpg");
		// if(!one.exists()){
		// one.createNewFile();
		// }
		// if(!two.exists()){
		// two.createNewFile();
		// }
		// }
		// }

		List<RaidItem> raidItems = new ArrayList<RaidItem>();
		RaidMember raidMember = RaidMember.findByName(name);
		List<RaidItem> mitems = RaidItem.find("order by raid desc").fetch();

		List<Raid> raids = new ArrayList<Raid>();

		if (raidMember != null) {
			PreparedStatement ps = DB.getConnection().prepareStatement("select r.id raidId from Raid r join RaidMember rm on (r.id = rm.raid_id) where BINARY rm.name = ? order by r.id desc");
			ps.setString(1, raidMember.name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				raids.add((Raid) Raid.findById(rs.getLong("raidId")));
			}

			for (RaidItem item : mitems) {
				if (raidMember.name.equals(item.member.name)) {
					raidItems.add(item);
				}
			}
		}

		Avatar avatar = Avatar.findById(id);
		List<AvatarItem> items = AvatarItem.getOrderedItemList(avatar);
		render(avatar, items, raidMember, raidItems, raids);
	}

	public static void showArmoryItemTooltip(Long avatarItemId) {
		AvatarItem item = AvatarItem.findById(avatarItemId);
		render(item);
	}

	public static void showItem(Long id) {
		Item item = Item.find("byItemId", id).first();
		if (item == null) {
			item = Item.setItem(id);
		}
		List<RaidItem> items = RaidItem.find("itemId = ?", item.itemId).fetch();
		List<AvatarItem> wearedItems = AvatarItem.find("itemId = ?", item.itemId).fetch();
		render(item, items, wearedItems);
	}

	public static void showItemTooltip(Long id) {
		Item item = Item.find("byItemId", id).first();
		if (item == null) {
			item = Item.setItem(id);
		}
		render(item);
	}

}
