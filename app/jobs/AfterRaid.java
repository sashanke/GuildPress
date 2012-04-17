package jobs;

import java.util.Date;
import java.util.List;

import models.wowapi.Armory;
import models.wowapi.character.Avatar;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.Recipe;
import models.wowapi.resources.CharacterSpec;
import models.wowapi.resources.Role;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;

@On("0 15 23 * * ?")
public class AfterRaid extends Job {
	@Override
	public void doJob() {
		Armory.setLastLogs();
	}
}
