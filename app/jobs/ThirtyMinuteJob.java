package jobs;

import java.io.File;

import models.wowapi.AuctionData;
import play.jobs.Every;
import play.jobs.Job;

@Every("30mn")
public class ThirtyMinuteJob extends Job {
	@Override
	public void doJob() {
		File lua = new File(play.Play.tmpDir + File.separator + "auctiondata" + File.separator + "Auc-ScanData.lua");
		if (lua.exists()) {
			AuctionData.parseFile(lua);
		}
		lua.delete();
	}
}