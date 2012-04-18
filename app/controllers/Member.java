package controllers;

import java.util.List;

import models.User;
import models.wowapi.character.Avatar;
import models.wowapi.resources.Realm;
import play.mvc.Before;
import play.mvc.Controller;

public class Member extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
	
	public static void index() {
		List<Realm> realms = Realm.all().fetch();
		render(realms);
	}
		
	public static void addAlt(String name, Long id) {
		
//		System.out.println(name);
//		System.out.println(session.get("lastPage"));
		
		Avatar alt = Avatar.findById(id);
		User user = User.getConnectedUser(session.get("username"));
		user.alts.add(alt);
		user.save();
		
		if (session.contains("lastPage")) {
			String redirect = session.get("lastPage");
			session.remove("lastPage");
			redirect(redirect);
		}
		renderText("OK");
	}
	
	public static void delAlt(Long id) {
		Avatar alt = Avatar.findById(id);
		User user = User.getConnectedUser(session.get("username"));
		user.alts.remove(alt);
		user.save();
		renderText("OK");
	}

	public static void settings() {
		render();
	}
	
	
}
