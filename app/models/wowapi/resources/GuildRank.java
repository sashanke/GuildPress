package models.wowapi.resources;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 
 * @author prime
 * 
 */
@Entity
public class GuildRank extends Model {

	@Required
	public Long rank;

	public String name;

	public GuildRank(Long rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return this.name + " (" + this.rank + ")";
	}
}