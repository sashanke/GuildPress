package models.wowapi.resources;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class RaceClassMap extends Model {

	@ManyToOne
	public Side side;

	@ManyToOne
	public CharacterRace race;

	@ManyToMany
	public List<CharacterClass> cclass;

	public RaceClassMap(Side side, CharacterRace race) {
		this.side = side;
		this.race = race;
		this.cclass = new ArrayList<CharacterClass>();
	}

	public static void createMap() {
		RaceClassMap.deleteAll();
		List<CharacterRace> crace = CharacterRace.findAll();
		for (CharacterRace crace2 : crace) {
			RaceClassMap rcmap = new RaceClassMap(crace2.side, crace2);
			List<Long> ids = new ArrayList<Long>();
			List<CharacterClass> cc;
			switch (rcmap.race.crId.intValue()) {
			case CharacterRace.MENSCH:
				ids.clear();
				ids.add(new Long(CharacterClass.TODESRITTER));
				//ids.add(new Long(CharacterClass.DRUIDE));
				ids.add(new Long(CharacterClass.JÄGER));
				ids.add(new Long(CharacterClass.MAGIER));
				ids.add(new Long(CharacterClass.PALADIN));
				ids.add(new Long(CharacterClass.PRIESTER));
				ids.add(new Long(CharacterClass.SCHURKE));
				//ids.add(new Long(CharacterClass.SCHAMANE));
				ids.add(new Long(CharacterClass.HEXENMEISTER));
				ids.add(new Long(CharacterClass.KRIEGER));
				cc = CharacterClass.find("ccId in (?1)", ids).fetch();
				for (CharacterClass characterClass : cc) {
					rcmap.cclass.add(characterClass);
				}
				rcmap.save();
				break;
			case CharacterRace.ZWERG:
				ids.clear();
				ids.add(new Long(CharacterClass.TODESRITTER));
				//ids.add(new Long(CharacterClass.DRUIDE));
				ids.add(new Long(CharacterClass.JÄGER));
				ids.add(new Long(CharacterClass.MAGIER));
				ids.add(new Long(CharacterClass.PALADIN));
				ids.add(new Long(CharacterClass.PRIESTER));
				ids.add(new Long(CharacterClass.SCHURKE));
				ids.add(new Long(CharacterClass.SCHAMANE));
				//ids.add(new Long(CharacterClass.HEXENMEISTER));
				ids.add(new Long(CharacterClass.KRIEGER));
				cc = CharacterClass.find("ccId in (?1)", ids).fetch();
				for (CharacterClass characterClass : cc) {
					rcmap.cclass.add(characterClass);
				}
				rcmap.save();
				break;
			case CharacterRace.NACHTELF:
				ids.clear();
				ids.add(new Long(CharacterClass.TODESRITTER));
				ids.add(new Long(CharacterClass.DRUIDE));
				ids.add(new Long(CharacterClass.JÄGER));
				ids.add(new Long(CharacterClass.MAGIER));
				//ids.add(new Long(CharacterClass.PALADIN));
				ids.add(new Long(CharacterClass.PRIESTER));
				ids.add(new Long(CharacterClass.SCHURKE));
				//ids.add(new Long(CharacterClass.SCHAMANE));
				//ids.add(new Long(CharacterClass.HEXENMEISTER));
				ids.add(new Long(CharacterClass.KRIEGER));
				cc = CharacterClass.find("ccId in (?1)", ids).fetch();
				for (CharacterClass characterClass : cc) {
					rcmap.cclass.add(characterClass);
				}
				rcmap.save();
				break;
			case CharacterRace.GNOM:
				ids.clear();
				ids.add(new Long(CharacterClass.TODESRITTER));
				//ids.add(new Long(CharacterClass.DRUIDE));
				//ids.add(new Long(CharacterClass.JÄGER));
				ids.add(new Long(CharacterClass.MAGIER));
				//ids.add(new Long(CharacterClass.PALADIN));
				ids.add(new Long(CharacterClass.PRIESTER));
				ids.add(new Long(CharacterClass.SCHURKE));
				//ids.add(new Long(CharacterClass.SCHAMANE));
				ids.add(new Long(CharacterClass.HEXENMEISTER));
				ids.add(new Long(CharacterClass.KRIEGER));
				cc = CharacterClass.find("ccId in (?1)", ids).fetch();
				for (CharacterClass characterClass : cc) {
					rcmap.cclass.add(characterClass);
				}
				rcmap.save();
				break;
			case CharacterRace.DRAENEI:
				ids.clear();
				ids.add(new Long(CharacterClass.TODESRITTER));
				//ids.add(new Long(CharacterClass.DRUIDE));
				ids.add(new Long(CharacterClass.JÄGER));
				ids.add(new Long(CharacterClass.MAGIER));
				ids.add(new Long(CharacterClass.PALADIN));
				ids.add(new Long(CharacterClass.PRIESTER));
				//ids.add(new Long(CharacterClass.SCHURKE));
				ids.add(new Long(CharacterClass.SCHAMANE));
				//ids.add(new Long(CharacterClass.HEXENMEISTER));
				ids.add(new Long(CharacterClass.KRIEGER));
				cc = CharacterClass.find("ccId in (?1)", ids).fetch();
				for (CharacterClass characterClass : cc) {
					rcmap.cclass.add(characterClass);
				}
				rcmap.save();
				break;
			case CharacterRace.WORGEN:
				ids.clear();
				ids.add(new Long(CharacterClass.TODESRITTER));
				ids.add(new Long(CharacterClass.DRUIDE));
				ids.add(new Long(CharacterClass.JÄGER));
				ids.add(new Long(CharacterClass.MAGIER));
				//ids.add(new Long(CharacterClass.PALADIN));
				ids.add(new Long(CharacterClass.PRIESTER));
				ids.add(new Long(CharacterClass.SCHURKE));
				//ids.add(new Long(CharacterClass.SCHAMANE));
				ids.add(new Long(CharacterClass.HEXENMEISTER));
				ids.add(new Long(CharacterClass.KRIEGER));
				cc = CharacterClass.find("ccId in (?1)", ids).fetch();
				for (CharacterClass characterClass : cc) {
					rcmap.cclass.add(characterClass);
				}
				rcmap.save();
				break;
			default:
				//TODO: Horde Kombis
				break;
			}
		}
	}

}
