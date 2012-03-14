package models.wowapi.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import models.Comment;

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
	
	public String toString() {
		return name + " (" + rank + ")";
	}
}