package models.raidtracker;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class RaidPool extends Model {
	public String name;
	public Boolean isMainRaid;

	public RaidPool(String name, Boolean isMainRaid) {
		this.name = name;
		this.isMainRaid = isMainRaid;
	}

}
