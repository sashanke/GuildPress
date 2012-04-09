package controllers;

import play.mvc.Before;
import play.mvc.Controller;

public class Progress extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void index() {
		render();
	}

}
