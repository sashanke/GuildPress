package controllers;

import play.mvc.Before;
import play.mvc.Controller;

public class FrontPage extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void index() {
		render();
	}
}
