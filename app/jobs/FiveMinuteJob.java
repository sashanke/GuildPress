package jobs;

import play.jobs.Every;
import play.jobs.Job;

@Every("5mn")
public class FiveMinuteJob extends Job {
	@Override
	public void doJob() {
		// Logger.info("[FiveMinuteJob][start]");
		// List<GuildMember> member =
		// GuildMember.find("lastUpdate < ? or avatar_id is null", new Date(new
		// Date().getTime() - Armory.QUATERDAYUPDATE)).fetch(3);
		// for (GuildMember guildMember : member) {
		// Avatar.createAsyncAvatar(guildMember.name, guildMember.realm.name);
		// }
		// Logger.info("[FiveMinuteJob][stop]");
	}
}
