package jobs;

import java.util.List;

import models.wowapi.character.Avatar;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.Recipe;
import models.wowapi.resources.CharacterSpec;
import models.wowapi.resources.Role;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("2mn")
public class Minute extends Job {
	@Override
	public void doJob() {


		if (Role.findAll().size() == 0) {
			Role.createRoles();
		}
		
		if (CharacterSpec.findAll().size() == 0) {
			CharacterSpec.createSpecs();
		}


		List<Recipe> recipes = Recipe.find("name is null").fetch(5);
		if (recipes.size() > 0) {
			Logger.info("[MinuteJob][setToolTip]");
			for (Recipe recipe : recipes) {
				recipe.setTooltip();
			}
		}
		
		List<GuildMember> guildMembers = GuildMember.find("avatar_id is null").fetch(2);
		if (guildMembers.size() > 0) {
			Logger.info("[MinuteJob][setAvatar]");
			for (GuildMember guildMember : guildMembers) {
				Avatar.createAvatar(guildMember.name, guildMember.realm.name);
			}
		}
		
		
	}
}
