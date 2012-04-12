package jobs;

import models.wowapi.Armory;
import models.wowapi.auction.Auction;
import models.wowapi.guild.Guild;
import play.jobs.Every;
import play.jobs.Job;

@Every("1h")
public class Hour extends Job{
	@Override
	public void doJob() {
		Guild.job();
		Auction.job();
		Armory.setLastLogs();
	}
}
