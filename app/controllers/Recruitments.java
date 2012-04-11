/**
 * 
 */
package controllers;

import java.util.ArrayList;
import java.util.List;

import models.recruitment.Recruitment;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterSpec;
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
}
