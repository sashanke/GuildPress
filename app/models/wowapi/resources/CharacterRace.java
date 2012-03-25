package models.wowapi.resources;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.Comment;

import play.db.jpa.Model;

/**
 * http://eu.battle.net/api/wow/data/avatar/races?locale=de_DE
 * 
 * @author prime
 * 
 */
@Entity
public class CharacterRace extends Model {

	public static final int DRAENEI = 11;
	public static final int MENSCH = 1;
	public static final int UNTOTER = 5;
	public static final int GNOM = 7;
	public static final int TROLL = 8;
	public static final int ORC = 2;
	public static final int ZWERG = 3;
	public static final int NACHTELF = 4;
	public static final int BLUTELF = 10;
	public static final int WORGEN = 22;
	public static final int TAUREN = 6;
	public static final int GOBLIN = 9;
	
	public Long crId;
	public Long mask;
	@ManyToOne
	public Side side;
	public String name;
	public Date lastUpdate;
	
	public CharacterRace(Long crId, Long mask, Side side, String name) {
		this.crId = crId;
		this.mask = mask;
		this.side = side;
		this.name = name;
		this.lastUpdate = new Date();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name + " (" + side.name + ")";
	}
	
}