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
public class Gender extends Model {

	@Required
	public Long gId;
	
	@Required
	public String name;

	public String name_loc;
	
	public Date lastUpdate;
	
	public Gender(Long gId, String name) {
		this.gId = gId;
		this.name = name;
		if (name.equals("male")) {
			this.name_loc = "männlich";
		}
		if (name.equals("female")) {
			this.name_loc = "weiblich";
		}
		this.lastUpdate = new Date();
	}
	public String toString() {
		return name;
	}
}