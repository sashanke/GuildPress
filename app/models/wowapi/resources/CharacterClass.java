package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;
import play.db.jpa.Model;

/**
 * http://eu.battle.net/api/wow/data/character/classes?locale=de_DE
 * @author prime
 *
 */
@Entity
public class CharacterClass extends Model {

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
		return name;
	}
	
}