package models.wowapi;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.wowapi.resources.Item;

import org.hibernate.annotations.Index;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import play.Logger;
import play.db.DB;
import play.db.jpa.Model;

@Entity
public class AuctionData extends Model {
	@ManyToOne
	public Item item;
	// LINK = 1
	String link;
	// ILEVEL = 2
	Long ilevel;
	// PRICE = 6
	Float price;
	// TLEFT = 7
	Long tleft;
	// TIME = 7
	Date time;
	// TIME = 8
	@Index(name = "idx_item_name")
	String name;
	// COUNT = 11
	Long count;
	// ULEVEL = 14
	Long ulevel;
	// MINBID = 15,
	Float minbid;
	// MININC = 16,
	Float mininc;
	// BUYOUT = 17,
	Float buyout;
	// CURBID = 18,
	Float curbid;
	// SELLER = 20
	@Index(name = "idx_seller")
	String seller;
	// ITEMID = 23
	@Index(name = "idx_itemid")
	Long itemid;
	// SEED = 27
	Long seed;
	@Index(name = "idx_updated")
	Date updated;
	@Index(name = "idx_scantime")
	Date scantime;

	// public AuctionData(Date updated, String link, Long ilevel, Float price,
	// Long tleft, Date time, String name, Long count, Long ulevel, Float
	// minbid, Float mininc, Float buyout, Float curbid, String seller, Long
	// itemid, Long seed) {
	// this.link = link;
	// this.ilevel = ilevel;
	// this.price = price;
	// this.tleft = tleft;
	// this.time = time;
	// this.name = name;
	// this.count = count;
	// this.ulevel = ulevel;
	// this.minbid = minbid;
	// this.mininc = mininc;
	// this.buyout = buyout;
	// this.curbid = curbid;
	// this.seller = seller;
	// this.itemid = itemid;
	// this.seed = seed;
	// this.updated = updated;
	// this.item = Item.setItem(this.itemid);
	// this.save();
	// }

	public static void parseFile(File lua) {
		Logger.info("[AuctionData][parseFile] " + lua);
		LuaValue _G = JsePlatform.standardGlobals();
		_G.get("dofile").call(LuaValue.valueOf(lua.getAbsolutePath()));
		LuaTable data = _G.get("AucScanData").get("scans").get("Anub'arak").get("Alliance").checktable();
		LuaTable scanstats = data.get("scanstats").checktable();

		Long lastFullScan = scanstats.get("LastFullScan").tolong() * 1000;
		AuctionData ad = AuctionData.find("byScantime", new Date(lastFullScan)).first();
		if (ad == null) {
			LuaTable guild = data.get("ropes").checktable();
			String scanData = guild.get(guild.getn()).toString();
			Logger.info("[AuctionData][parseFile] File Parsed " + lua);

			scanData = scanData.substring(8);
			scanData = scanData.substring(0, scanData.length() - 2);

			Pattern pattern = Pattern.compile("(?ism)(\\{(.*?),\\})");
			Matcher matcher = pattern.matcher(scanData);

			Logger.info("[AuctionData][parseFile] File Regexed " + lua);

			try {
				Connection con = DB.getConnection();
				con.setAutoCommit(false);
				PreparedStatement ps = con.prepareStatement("INSERT INTO AuctionData (`buyout`, `count`, `curbid`, `ilevel`, `itemid`, `link`, `minbid`, `mininc`, `name`, `price`, `seed`, `seller`, `time`, `tleft`, `ulevel`, `updated`, `item_id`, `scantime`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				
				int cnt = 0;
				int comcnt = 0;
				while (matcher.find()) {
					cnt++;
					String[] itemData = matcher.group(2).split(",");
					// LINK = 1
					String link = itemData[0];
					// ILEVEL = 2
					Long ilevel = Long.parseLong(itemData[1]);
					// PRICE = 6
					Float price = Float.parseFloat(itemData[5]);
					// TLEFT = 7
					Long tleft = Long.parseLong(itemData[6]);
					// TIME = 7
					Date time = new Date(0L);
					if (!itemData[7].equals("nil")) {
						time.setTime(Long.parseLong(itemData[7]) * 1000);
					}
					// TIME = 8
					String name = itemData[8];
					// COUNT = 11
					Long count = Long.parseLong(itemData[10]);
					// ULEVEL = 14
					Long ulevel = Long.parseLong(itemData[13]);
					// MINBID = 15,
					Float minbid = Float.parseFloat(itemData[14]);
					// MININC = 16,
					Float mininc = Float.parseFloat(itemData[15]);
					// BUYOUT = 17,
					Float buyout = Float.parseFloat(itemData[16]);
					// CURBID = 18,
					Float curbid = Float.parseFloat(itemData[17]);
					// SELLER = 20
					String seller = itemData[19];
					// ITEMID = 23
					Long itemid = Long.parseLong(itemData[22]);
					// SEED = 27
					Long seed = Long.parseLong(itemData[26]);

					//`buyout`, `count`, `curbid`, `ilevel`, `itemid`, `link`, `minbid`, `mininc`, `name`, `price`, `seed`, `seller`, `time`, `tleft`, `ulevel`, `updated`, `item_id`, `scantime`
					ps.setFloat(1, buyout);
					ps.setLong(2, count);
					ps.setFloat(3, curbid);
					ps.setLong(4, ilevel);
					ps.setLong(5, itemid);
					ps.setString(6, link);
					ps.setFloat(7, minbid);
					ps.setFloat(8, mininc);
					ps.setString(9, name);
					ps.setFloat(10, price);
					ps.setLong(11, seed);
					ps.setString(12, seller);
					ps.setTimestamp(13, new Timestamp(time.getTime()));
					ps.setLong(14, tleft);
					ps.setLong(15, ulevel);
					ps.setTimestamp(16, new Timestamp(0L));
					ps.setNull(17, 0);
					ps.setTimestamp(18, new Timestamp(lastFullScan));

					ps.executeUpdate();
					comcnt++;
					
					if (comcnt == 500) {
						comcnt = 0;
						Logger.info("[AuctionData][parseFile] " + cnt + " Commit " + lua);
						con.commit();
					}
				}
				con.commit();
				Logger.info("[AuctionData][parseFile] " + cnt + " Auctions Found " + lua);
				setItems(lastFullScan);
				updatePrices(lastFullScan);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Logger.info("[AuctionData][parseFile] Data from " + new Date(lastFullScan) + " already in the Database " + lua);
		}

	}

	private static void updatePrices(Long lastFullScan) throws SQLException {
//		Logger.info("[AuctionData][updatePrices] Updating Item Prices... ");
//		Connection con = DB.getConnection();
//		PreparedStatement ps = con.prepareStatement("SELECT DISTINCT itemid FROM AuctionData");
//		ResultSet rs = ps.executeQuery();
//		while (rs.next()) {
//			System.out.println(rs.getLong("itemId"));
//		}
//		
//		Logger.info("[AuctionData][updatePrices] Updating Item Prices finished ");
		
	}

	private static void setItems(Long lastFullScan) throws SQLException {
		Logger.info("[AuctionData][setItems] Updating Items... ");
		Connection con = DB.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT DISTINCT itemid FROM AuctionData WHERE item_id is null and scantime = ?");
		PreparedStatement psUpdate = con.prepareStatement("UPDATE AuctionData SET item_id = ? WHERE itemid = ? and scantime = ?");
		ps.setTimestamp(1, new Timestamp(lastFullScan));
		ResultSet rs = ps.executeQuery();

		HashMap<Long, Long> items = new HashMap<Long, Long>();
		
		PreparedStatement psItem = con.prepareStatement("SELECT DISTINCT id, itemId FROM Item");
		ResultSet rsItem = psItem.executeQuery();
		while (rsItem.next()) {
			items.put(rsItem.getLong("itemId"), rsItem.getLong("id"));
		}

		while (rs.next()) {
			psUpdate.setLong(1, items.get(rs.getLong("itemid")));
			psUpdate.setLong(2, rs.getLong("itemid"));
			psUpdate.setTimestamp(3, new Timestamp(lastFullScan));
			psUpdate.executeUpdate();
		}
		
		List<AuctionData> ad = AuctionData.find("item_id is null").fetch();
		for (AuctionData auctionData : ad) {
			auctionData.item = Item.setItem(auctionData.itemid);
		}
		Logger.info("[AuctionData][setItems] Updating Items finished ");
	}
}
