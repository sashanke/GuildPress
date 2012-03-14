package controllers;

import play.mvc.*;

public class RaidFortschritt extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
    public static void index() {
        render();
    }

}
