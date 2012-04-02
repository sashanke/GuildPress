package jobs;

import java.util.Date;
import java.util.List;

import com.google.gson.JsonElement;

import controllers.Service;

import models.wowapi.Armory;
import models.wowapi.character.Avatar;
import models.wowapi.guild.Guild;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.Gender;
import models.wowapi.resources.Side;
import play.Logger;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.WS;
import play.libs.F.Promise;
import play.libs.WS.HttpResponse;

//@OnApplicationStart
@Every("30mn")
public class UpdateJob extends Job {
	public void doJob() {
		
		//http://eu.battle.net/api/wow/realm/status?locale=de_DE
		String url = "http://eu.battle.net/api/wow/realm/status?locale=de_DE";
		
		Logger.info("Test Armory Fetch URL: " + url);

		HttpResponse hr = WS.url(url).get();
		if (hr.success()) {
			Logger.error("Armory ping: (" + hr.getStatus() + ") " + url);
			


			
			
//			List<GuildMember> guildMember = GuildMember.findAll();
//			for (GuildMember guildMember2 : guildMember) {
//				Promise<Avatar> futureAvatar = Avatar.createAsyncAvatar(guildMember2.name, guildMember2.realm.name);
//				futureAvatar.getOrNull();
//			}
			
			//new Armory();
		} else {
			Logger.error("Fetch failed: (" + hr.getStatus() + ") " + url);

		}
	}
}
