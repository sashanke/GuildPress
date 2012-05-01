package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.wowapi.character.Avatar;

import play.db.jpa.Model;
@Entity
public class Event extends Model {
	
	public Date eventStart;
	public Date eventEnd;
	public String title;
	public String info;
	
	@ManyToOne
	public Avatar manager;
	
	@ManyToOne
	public EventType type;
	
	@OneToMany
	public List<EventAttendee> attendees;
	
	public Long size;
	
	public Event(Date eventStart, Date eventEnd, String title,String info, Avatar manager, Long size) {
		this.eventStart = eventStart;
		this.eventEnd = eventEnd;
		this.title = title;
		this.info = info;
		this.manager = manager;
		this.attendees = new ArrayList<EventAttendee>();
		this.size = size;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;
	}
}
