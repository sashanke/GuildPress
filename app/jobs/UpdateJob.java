package jobs;

import java.util.List;

import models.wowapi.Armory;
import models.wowapi.guild.GuildMember;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
@Every("1mn")
public class UpdateJob extends Job {
	 public void doJob() {
		 new Armory();
		 
		 List<GuildMember> gm = GuildMember.find("hasWoWCharacter = ?", false).fetch();
		 for (GuildMember guildMember : gm) {
			System.out.println(guildMember.name);
		}
		 
	    }
}
