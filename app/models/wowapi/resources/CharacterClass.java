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

	public static final int JÄGER = 3;
	public static final int SCHURKE = 4;
	public static final int KRIEGER = 1;
	public static final int PALADIN = 2;
	public static final int SCHAMANE = 7;
	public static final int MAGIER = 8;
	public static final int PRIESTER = 5;
	public static final int TODESRITTER = 6;
	public static final int DRUIDE = 11;
	public static final int HEXENMEISTER = 9;

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