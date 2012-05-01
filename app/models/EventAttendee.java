package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.types.Confirmation;
import models.wowapi.character.Avatar;

import play.db.jpa.Model;

@Entity
public class EventAttendee extends Model {
	
	public Confirmation confirmation;
	
	public Date lastUpdate;
	
	@ManyToOne
	public Avatar avatar;
	
	@ManyToOne
	public Event event;
	
	public EventAttendee(Confirmation confirmation, Avatar avatar, Date lastUpdate, Event event) {
		this.confirmation = confirmation;
		this.avatar = avatar;
		this.lastUpdate = lastUpdate;
		this.event = event;
		this.save();
	}
}
