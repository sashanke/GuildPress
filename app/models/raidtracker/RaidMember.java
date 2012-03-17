package models.raidtracker;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.wowapi.character.Avatar;

import play.db.jpa.Model;

@Entity
public class RaidMember extends Model {

	public String name;

	public Date betreten;
	public Date verlassen;

	@ManyToOne
	public Avatar avatar;
	@ManyToOne
	public Raid raid;
	
	public RaidMember(String name, Date betreten, Date verlassen, Avatar avatar, Raid raid) {
		this.name = name;
		this.betreten = betreten;
		this.verlassen = verlassen;
		this.avatar = avatar;
		this.raid = raid;
	}

}
