package jobs;

import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.libs.WS;
import play.libs.WS.HttpResponse;

//@OnApplicationStart
@Every("30mn")
public class UpdateJob extends Job {
	@Override
	public void doJob() {

		// http://eu.battle.net/api/wow/realm/status?locale=de_DE
		String url = "http://eu.battle.net/api/wow/realm/status?locale=de_DE";

		Logger.info("Test Armory Fetch URL: " + url);

		HttpResponse hr = WS.url(url).get();
		if (hr.success()) {
			Logger.error("Armory ping: (" + hr.getStatus() + ") " + url);

			// List<GuildMember> guildMember = GuildMember.findAll();
			// for (GuildMember guildMember2 : guildMember) {
			// Promise<Avatar> futureAvatar =
			// Avatar.createAsyncAvatar(guildMember2.name,
			// guildMember2.realm.name);
			// futureAvatar.getOrNull();
			// }

			// new Armory();
		} else {
			Logger.error("Fetch failed: (" + hr.getStatus() + ") " + url);

		}
	}
}
