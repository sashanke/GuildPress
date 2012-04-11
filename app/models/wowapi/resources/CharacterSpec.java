package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * http://eu.battle.net/api/wow/data/avatar/classes?locale=de_DE
 * 
 * @author prime
 * 
 */
@Entity
public class CharacterSpec extends Model {

	@ManyToOne
	public CharacterClass cclass;

	@ManyToOne
	public Icon icon;

	@ManyToOne
	public Role role;

	public String name;

	public CharacterSpec(String name, CharacterClass cclass, Icon icon, Role role) {
		this.name = name;
		this.cclass = cclass;
		this.icon = icon;
		this.role = role;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.cclass.name + " " + this.name;
	}

	public static void createSpecs() {
		// Druid
		new CharacterSpec("Balance", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.DRUIDE).first(), Icon.setIcon(9999011L, "spell_nature_starfall"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Feral", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.DRUIDE).first(), Icon.setIcon(9999012L, "ability_druid_catform"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Guardian", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.DRUIDE).first(), Icon.setIcon(9999013L, "ability_racial_bearform"), (Role) Role.find("name = ?", "tank").first()).save();
		new CharacterSpec("Restoration", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.DRUIDE).first(), Icon.setIcon(9999014L, "spell_nature_healingtouch"), (Role) Role.find("name = ?", "healer").first()).save();

		// Warlock
		new CharacterSpec("Affliction", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.HEXENMEISTER).first(), Icon.setIcon(9999015L, "spell_shadow_deathcoil"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Demonology", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.HEXENMEISTER).first(), Icon.setIcon(9999016L, "spell_shadow_metamorphosis"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Destruction", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.HEXENMEISTER).first(), Icon.setIcon(9999017L, "spell_shadow_rainoffire"), (Role) Role.find("name = ?", "dps").first()).save();

		// Hunter
		new CharacterSpec("Beast Mastery", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.JÄGER).first(), Icon.setIcon(9999018L, "ability_hunter_bestialdiscipline"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Marksmanship", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.JÄGER).first(), Icon.setIcon(9999019L, "ability_hunter_focusedaim"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Survival", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.JÄGER).first(), Icon.setIcon(9999020L, "ability_hunter_camouflage"), (Role) Role.find("name = ?", "dps").first()).save();

		// Warrior
		new CharacterSpec("Arms", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.KRIEGER).first(), Icon.setIcon(9999021L, "ability_warrior_savageblow"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Fury", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.KRIEGER).first(), Icon.setIcon(9999022L, "ability_warrior_innerrage"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Protection", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.KRIEGER).first(), Icon.setIcon(9999023L, "ability_warrior_defensivestance"), (Role) Role.find("name = ?", "tank").first()).save();

		// Mage
		new CharacterSpec("Arcane", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.MAGIER).first(), Icon.setIcon(9999024L, "spell_holy_magicalsentry"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Fire", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.MAGIER).first(), Icon.setIcon(9999025L, "spell_fire_firebolt02"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Frost", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.MAGIER).first(), Icon.setIcon(9999026L, "spell_frost_frostbolt02"), (Role) Role.find("name = ?", "dps").first()).save();

		// Paladin
		new CharacterSpec("Holy", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.PALADIN).first(), Icon.setIcon(9999027L, "spell_holy_holybolt"), (Role) Role.find("name = ?", "healer").first()).save();
		new CharacterSpec("Protection", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.PALADIN).first(), Icon.setIcon(9999028L, "ability_paladin_shieldofthetemplar"), (Role) Role.find("name = ?", "tank").first()).save();
		new CharacterSpec("Retribution", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.PALADIN).first(), Icon.setIcon(9999029L, "spell_holy_auraoflight"), (Role) Role.find("name = ?", "dps").first()).save();
		
		// Priest
		new CharacterSpec("Discipline", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.PRIESTER).first(), Icon.setIcon(9999030L, "spell_holy_powerwordshield"), (Role) Role.find("name = ?", "healer").first()).save();
		new CharacterSpec("Holy", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.PRIESTER).first(), Icon.setIcon(9999031L, "spell_holy_guardianspirit"), (Role) Role.find("name = ?", "healer").first()).save();
		new CharacterSpec("Shadow", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.PRIESTER).first(), Icon.setIcon(9999032L, "spell_shadow_shadowwordpain"), (Role) Role.find("name = ?", "dps").first()).save();
				
		// Schaman
		new CharacterSpec("Elemental", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.SCHAMANE).first(), Icon.setIcon(9999033L, "spell_nature_lightning"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Enhancement", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.SCHAMANE).first(), Icon.setIcon(9999034L, "spell_shaman_improvedstormstrike"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Restoration", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.SCHAMANE).first(), Icon.setIcon(9999035L, "spell_nature_magicimmunity"), (Role) Role.find("name = ?", "healer").first()).save();
		
		// Rogue
		new CharacterSpec("Assassination", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.SCHURKE).first(), Icon.setIcon(9999036L, "ability_rogue_eviscerate"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Combat", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.SCHURKE).first(), Icon.setIcon(9999037L, "ability_backstab"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Subtlety", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.SCHURKE).first(), Icon.setIcon(9999038L, "ability_stealth"), (Role) Role.find("name = ?", "dps").first()).save();
		
		// Deathknight
		new CharacterSpec("Blood", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.TODESRITTER).first(), Icon.setIcon(9999039L, "spell_deathknight_bloodpresence"), (Role) Role.find("name = ?", "tank").first()).save();
		new CharacterSpec("Frost", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.TODESRITTER).first(), Icon.setIcon(9999040L, "spell_deathknight_frostpresence"), (Role) Role.find("name = ?", "dps").first()).save();
		new CharacterSpec("Unholy", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.TODESRITTER).first(), Icon.setIcon(9999041L, "spell_deathknight_unholypresence"), (Role) Role.find("name = ?", "dps").first()).save();
		
		// TODO Monk!
		// Monk
		new CharacterSpec("Brewmaster", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.MÖNCH).first(), Icon.setIcon(9999043L, "spell_monk_brewmaster_spec"), (Role) Role.find("name = ?", "tank").first()).save();
		new CharacterSpec("Mistweaver", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.MÖNCH).first(), Icon.setIcon(9999044L, "spell_monk_mistweaver_spec"), (Role) Role.find("name = ?", "healer").first()).save();
		new CharacterSpec("Windwalker", (CharacterClass) CharacterClass.find("byCcId", CharacterClass.MÖNCH).first(), Icon.setIcon(9999045L, "spell_monk_windwalker_spec"), (Role) Role.find("name = ?", "dps").first()).save();
		
		
	}

}