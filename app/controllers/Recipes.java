package controllers;

import java.util.List;

import models.wowapi.character.AvatarProfession;
import models.wowapi.character.helper.AvatarProfessionHelper;
import play.mvc.Before;
import play.mvc.Controller;

public class Recipes extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
	
	public static void index() {
		render();
	}
	
	public static void showProfession(Long id, String name) {
		AvatarProfession profession = AvatarProfession.find("byProfId", id).first();
		
		List<AvatarProfessionHelper> recipes = AvatarProfessionHelper.getList(profession);
		
		render(profession,recipes);
	}
}
