package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * http://eu.battle.net/api/wow/data/avatar/classes?locale=de_DE
 * 
 * @author prime
 * 
 */
@Entity
public class CharacterClass extends Model {

	public static final long JÄGER = 3;
	public static final long SCHURKE = 4;
	public static final long KRIEGER = 1;
	public static final long PALADIN = 2;
	public static final long SCHAMANE = 7;
	public static final long MAGIER = 8;
	public static final long PRIESTER = 5;
	public static final long TODESRITTER = 6;
	public static final long DRUIDE = 11;
	public static final long HEXENMEISTER = 9;
	//TODO Monk!
	public static final long MÖNCH = 12;

	public Long ccId;
	public Long mask;
	public String name;
	public String powerType;

	public Boolean isSpell = false;
	public Date lastUpdate;

	public CharacterClass(Long ccId, Long mask, String name, String powerType) {
		this.ccId = ccId;
		this.mask = mask;
		this.name = name;
		this.powerType = powerType;
		this.lastUpdate = new Date();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

}