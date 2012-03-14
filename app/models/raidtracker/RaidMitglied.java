package models.raidtracker;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.wowapi.character.WoWCharacter;

import play.db.jpa.Model;

@Entity
public class RaidMitglied extends Model {

	public String name;

	public Date betreten;
	public Date verlassen;

	@ManyToOne
	public WoWCharacter charakter;
	@ManyToOne
	public Raid raid;
	
	public RaidMitglied(String name, Date betreten, Date verlassen, WoWCharacter charakter, Raid raid) {
		this.name = name;
		this.betreten = betreten;
		this.verlassen = verlassen;
		this.charakter = charakter;
		this.raid = raid;
	}

}
