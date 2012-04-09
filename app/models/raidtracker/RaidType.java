package models.raidtracker;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class RaidType extends Model {
	public Long modus;
	public String name;

	public RaidType(Long modus, String name) {
		this.modus = modus;
		this.name = name;
	}
}
