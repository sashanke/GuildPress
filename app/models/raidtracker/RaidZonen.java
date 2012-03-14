package models.raidtracker;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class RaidZonen extends Model {
	
	public Date betreten;
	public Date verlassen;
	public String name;
	@ManyToOne
	public RaidModus modus;
	
	@ManyToOne
	public Raid raid;
	
	public RaidZonen(Date betreten, Date verlassen, String name, RaidModus modus, Raid raid) {
		this.betreten = betreten;
		this.verlassen = verlassen;
		this.name = name;
		this.modus = modus;
		this.raid = raid;
	}
	
	
}
