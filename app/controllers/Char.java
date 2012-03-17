package controllers;

import models.wowapi.character.Avatar;
import play.mvc.Before;
import play.mvc.Controller;

public class Char extends Controller {
	
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
	
	public static void show(String name) {
		Avatar avatar = Avatar.find("name = ?", name).first();
		render(avatar);
	}
}
