package models.wowapi.resources;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Skill extends Model {

	public static void createSkills() {
		if (Skill.count() == 0) {
			new Skill(171L, "Alchemie", Icon.setIcon(171L, "trade_alchemy"));

			new Skill(171L, "Alchemie", Icon.setIcon(171L, "trade_alchemy"));
			new Skill(164L, "Schmiedekunst", Icon.setIcon(164L, "trade_blacksmithing"));
			new Skill(333L, "Verzauberkunst", Icon.setIcon(333L, "trade_engraving"));
			new Skill(202L, "Ingenieurskunst", Icon.setIcon(202L, "trade_engineering"));
			new Skill(182L, "Kräuterkunde", Icon.setIcon(182L, "spell_nature_naturetouchgrow"));
			new Skill(773L, "Inschriftenkunde", Icon.setIcon(773L, "inv_inscription_tradeskill01"));
			new Skill(755L, "Juwelenschleifen", Icon.setIcon(755L, "inv_misc_gem_01"));
			new Skill(165L, "Lederverarbeitung", Icon.setIcon(165L, "inv_misc_armorkit_17"));

			new Skill(186L, "Bergbau", Icon.setIcon(186L, "trade_mining"));
			new Skill(393L, "Kürschnerei", Icon.setIcon(393L, "inv_misc_pelt_wolf_01"));
			new Skill(197L, "Schneiderei", Icon.setIcon(197L, "trade_tailoring"));
			new Skill(794L, "Archäologie", Icon.setIcon(794L, "inv_misc_rune_06"));
			new Skill(185L, "Kochkunst", Icon.setIcon(185L, "inv_misc_food_15"));
			new Skill(129L, "Erste Hilfe", Icon.setIcon(129L, "spell_holy_sealofsacrifice"));
			new Skill(356L, "Angeln", Icon.setIcon(356L, "trade_fishing"));
			new Skill(762L, "Reiten", Icon.setIcon(762L, "spell_nature_swiftness"));
		}

	}

	public Long skill;

	public String name;

	@ManyToOne
	public Icon icon;

	// [755,"Juwelenschleifen","/skill=755",,{tinyIcon:"inv_misc_gem_01"}],
	// [165,"Lederverarbeitung","/skill=165",,{tinyIcon:"inv_misc_armorkit_17"}],

	// [186,"Bergbau","/skill=186",,{tinyIcon:"trade_mining"}],
	// [393,"Kürschnerei","/skill=393",,{tinyIcon:"inv_misc_pelt_wolf_01"}],
	// [197,"Schneiderei","/skill=197",,{tinyIcon:"trade_tailoring"}],
	// [794,"Archäologie","/skill=794",,{tinyIcon:"inv_misc_rune_06"}],
	// [185,"Kochkunst","/skill=185",,{tinyIcon:"inv_misc_food_15"}],
	// [129,"Erste Hilfe","/skill=129",,{tinyIcon:"spell_holy_sealofsacrifice"}],
	// [356,"Angeln","/skill=356",,{tinyIcon:"trade_fishing"}],
	// [762,"Reiten","/skill=762",,{tinyIcon:"spell_nature_swiftness"}

	public Skill(long skill, String name, Icon icon) {
		this.skill = skill;
		this.name = name;
		this.icon = icon;
		this.save();
	}
	// [171,"Alchemie","/skill=171",,{tinyIcon:"trade_alchemy"}]
	// [164,"Schmiedekunst","/skill=164",,{tinyIcon:"trade_blacksmithing"}],
	// [333,"Verzauberkunst","/skill=333",,{tinyIcon:"trade_engraving"}],
	// [202,"Ingenieurskunst","/skill=202",,{tinyIcon:"trade_engineering"}],
	// [182,"Kräuterkunde","/skill=182",,{tinyIcon:"spell_nature_naturetouchgrow"}],
	// [773,"Inschriftenkunde","/skill=773",,{tinyIcon:"inv_inscription_tradeskill01"}],

}
