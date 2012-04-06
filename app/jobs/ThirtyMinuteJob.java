package jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.AuctionData;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("30mn")
public class ThirtyMinuteJob extends Job {
	public void doJob() {
		File lua = new File(play.Play.tmpDir + File.separator + "auctiondata" + File.separator + "Auc-ScanData.lua");
		if (lua.exists()) {
			Logger.info("[ThirtyMinuteJob][PrecessingAucionData] :" + lua);
			StringBuilder sb = new StringBuilder();
			try {
				BufferedReader in = new BufferedReader(new FileReader(lua));
				String str;
				while ((str = in.readLine()) != null) {
					sb.append(str + "\n");
				}
				in.close();
			} catch (IOException e) {
			}

			String luaString = sb.toString();
			String dataString = "";
			Date updated = new Date();
			Pattern pattern = Pattern.compile("(?ism)(.*?)(\\[\"ropes\"\\] = \\{.*?\"return \\{)(.*?)(\\},\\}\", --.*)");
			Matcher matcher = pattern.matcher(luaString);
			if (matcher.find()) {
				luaString = matcher.group(3);
				dataString = matcher.group(1) + matcher.group(2) + matcher.group(4);
			}

			pattern = Pattern.compile("(?ism)(\\[\"ImageUpdated\"\\] = )([0-9]{9,12})");
			matcher = pattern.matcher(dataString);
			if (matcher.find()) {
				updated.setTime(Long.parseLong(matcher.group(2)) * 1000);
			}

			AuctionData ad = AuctionData.find("updated = ?", updated).first();
			if (ad == null) {
				String[] itemLines = luaString.split("\\},\\{");

				for (String string : itemLines) {
					String auctionLine = string.replaceAll("\\\\\"", "");
					auctionLine = auctionLine.substring(0, auctionLine.length() - 1);
					String[] itemData = auctionLine.split(",");
					// |cffa335ee|Hitem:71980:0:0:0:0:0:0:319052448:80:0|h[Beinwickel
					// des
					// Lavabebens]|h|r\",397,\"Rüstung\",\"Stoff\",7,180000000,3,1333684326,\"Beinwickel
					// des
					// Lavabebens\",\"Interface\\\\Icons\\\\inv_pants_robe_raidmage_k_01\",1,4,1,85,180000000,0,200000000,0,false,\"Lacure\",0,1,71980,0,0,0,319052448

					// LINK = 1
					String link = itemData[0];
					// ILEVEL = 2
					Long ilevel = Long.parseLong(itemData[1]);
					// PRICE = 6
					Float price = Float.parseFloat(itemData[5]);
					// TLEFT = 7
					Long tleft = Long.parseLong(itemData[6]);
					// TIME = 7
					Date time = new Date();
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

					new AuctionData(updated, link, ilevel, price, tleft, time, name, count, ulevel, minbid, mininc, buyout, curbid, seller, itemid, seed);

				}
			}
		}
		
		lua.delete();
	}
}
