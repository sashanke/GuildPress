package controllers;

import play.mvc.*;

public class Progress extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
    public static void index() {
        render();
    }

}
