package controllers;

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
		render();
	}
}
