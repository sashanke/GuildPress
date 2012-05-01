/**
 * 
 */
package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.User;
import models.recruitment.Recruitment;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterSpec;
import play.libs.Crypto;
import play.libs.Time;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * @author prime
 * 
 */
public class Recruitments extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void index() {
		Recruitment recruitment = Recruitment.findById(1L);
		
		List<CharacterClass> classes = new ArrayList<CharacterClass>();
		List<CharacterSpec> specs = recruitment.specs;
		for (CharacterSpec characterSpec : specs) {
			if (!classes.contains(characterSpec.cclass)) {
				classes.add(characterSpec.cclass);
			}
		}
		List<CharacterSpec> allSpecs = CharacterSpec.findAll();
		render(recruitment,classes,allSpecs);
	}
	
	public static void apply(Long id, String specName, String className) {
		User user = User.getConnectedUser(session.get("username"));
		if (user == null) {
			Date expiration = new Date();
            String duration = "10d";  // maybe make this override-able 
            expiration.setTime(expiration.getTime() + Time.parseDuration(duration));
            response.setCookie("recruitmentApply",id.toString(), duration);
            info();
		}
		CharacterSpec spec = CharacterSpec.findById(id);
		render(spec);
	}

	public static void info() {
		render();
	}
	
}
