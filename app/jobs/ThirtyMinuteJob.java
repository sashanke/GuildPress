package jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.wowapi.AuctionData;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("30mn")
public class ThirtyMinuteJob extends Job {
	public void doJob() {
		File lua = new File(play.Play.tmpDir + File.separator + "auctiondata" + File.separator + "Auc-ScanData.lua");
		if (lua.exists()) {
			AuctionData.parseFile(lua);
		}
		lua.delete();
	}
}