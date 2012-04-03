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
import models.wowapi.resources.Recipe;
import models.wowapi.resources.Side;
import play.Logger;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.WS;
import play.libs.F.Promise;
import play.libs.WS.HttpResponse;

@Every("1mn")
public class MinuteJob extends Job {
	public void doJob() {
		Logger.info("[MinuteJob][start]");
		List<Recipe> recipes = Recipe.find("name is null").fetch(5);
		for (Recipe recipe : recipes) {
			recipe.setTooltip();
		}

		recipes = Recipe.find("item_id is null and name is not null and isEffect is null").fetch();
		for (Recipe recipe : recipes) {
			recipe.setTooltip();
		}
		
		
//		recipes = Recipe.find("item_id is null and name is not null").fetch();
//		for (Recipe recipe : recipes) {
//			recipe.setTooltip();
//		}
		
		Logger.info("[MinuteJob][stop]");
	}
}
