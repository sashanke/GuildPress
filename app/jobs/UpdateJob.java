package jobs;

import java.util.List;

import models.wowapi.Armory;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.Gender;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
@Every("1mn")
public class UpdateJob extends Job {
	public void doJob() {
		
		
		
		new Armory();

//		List<Gender> g = Gender.findAll();
//		for (Gender gender : g) {
//			if (gender.name.equals("male")) {
//				gender.name_loc = "männlich";
//			}
//			if (gender.name.equals("female")) {
//				gender.name_loc = "weiblich";
//			}
//			gender.save();
//		}
	}
}
