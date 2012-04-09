package jobs;

import java.util.List;

import models.wowapi.resources.Recipe;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("1mn")
public class MinuteJob extends Job {
	@Override
	public void doJob() {
		List<Recipe> recipes = Recipe.find("name is null").fetch(5);
		if (recipes.size() > 0) {
			Logger.info("[MinuteJob][setToolTip]");
			for (Recipe recipe : recipes) {
				recipe.setTooltip();
			}
		}
	}
}
