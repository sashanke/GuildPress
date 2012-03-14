package models.raidtracker;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class RaidModus extends Model {
	public Long modus;
	public String name;
	
	public RaidModus(Long modus, String name) {
		this.modus = modus;
		this.name = name;
	}
}
