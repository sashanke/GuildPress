package jobs;

import java.util.List;

import com.google.gson.JsonElement;

import models.wowapi.Armory;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.Gender;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.WS;
import play.libs.WS.HttpResponse;

@OnApplicationStart
@Every("30mn")
public class UpdateJob extends Job {
	public void doJob() {
		
		//http://eu.battle.net/api/wow/realm/status?locale=de_DE
		String url = "http://eu.battle.net/api/wow/realm/status?locale=de_DE";
		
		Logger.info("Test Armory Fetch URL: " + url);

		HttpResponse hr = WS.url(url).get();
		if (hr.success()) {
			Logger.error("Armory ping: (" + hr.getStatus() + ") " + url);
			new Armory();
		} else {
			Logger.error("Fetch failed: (" + hr.getStatus() + ") " + url);

		}
	}
}
