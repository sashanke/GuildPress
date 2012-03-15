package models.raidtracker;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.wowapi.character.Character;

import play.db.jpa.Model;

@Entity
public class RaidMember extends Model {

	public String name;

	public Date betreten;
	public Date verlassen;

	@ManyToOne
	public Character character;
	@ManyToOne
	public Raid raid;
	
	public RaidMember(String name, Date betreten, Date verlassen, Character character, Raid raid) {
		this.name = name;
		this.betreten = betreten;
		this.verlassen = verlassen;
		this.character = character;
		this.raid = raid;
	}

}
