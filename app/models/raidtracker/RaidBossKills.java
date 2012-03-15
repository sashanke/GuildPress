package models.raidtracker;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class RaidBossKills extends Model {
	public String name;
	public Date date;
	@ManyToOne
	public RaidType modus;
	@ManyToOne
	public Raid raid;
	
	public RaidBossKills(String name, Date date, RaidType modus, Raid raid) {
		this.name = name;
		this.date = date;
		this.modus = modus;
		this.raid = raid;
	}
}
