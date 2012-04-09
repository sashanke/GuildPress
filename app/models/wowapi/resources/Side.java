package models.wowapi.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 
 * @author prime
 * 
 */
@Entity
public class Side extends Model {

	@Required
	public Long sId;

	@Required
	public String name;

	@OneToMany(mappedBy = "side", cascade = CascadeType.ALL)
	public List<CharacterRace> races;

	Date lastUpdate;

	public Side(Long sId, String name) {
		this.races = new ArrayList<CharacterRace>();
		this.sId = sId;
		this.name = name;
		this.lastUpdate = new Date();
	}

	@Override
	public String toString() {
		return this.name;
	}
}