package models.wowapi.resources;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class SkillCategorie extends Model {
	// {"-6":"Haustiere","-5":"Reittiere","-4":"Völkerfertigkeiten","6":"Waffenfertigkeiten","8":"Rüstungssachverstand","9":"Nebenberufe","10":"Sprachen","11":"Berufe"}
	public static void createSkillCategories() {
		if (SkillCategorie.count() == 0) {
			new SkillCategorie(-6L, "Haustiere");
			new SkillCategorie(-5L, "Reittiere");
			new SkillCategorie(-4L, "Völkerfertigkeiten");
			new SkillCategorie(6L, "Waffenfertigkeiten");
			new SkillCategorie(8L, "Rüstungssachverstand");
			new SkillCategorie(9L, "Nebenberufe");
			new SkillCategorie(10L, "Sprachen");
			new SkillCategorie(11L, "Berufe");

		}

	}

	public Long cat;
	public String name;

	public SkillCategorie(long cat, String name) {
		this.cat = cat;
		this.name = name;
		this.save();
	}
}
