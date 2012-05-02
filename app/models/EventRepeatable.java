package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableInterval;

import models.wowapi.character.Avatar;

import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
public class EventRepeatable extends Model {

	public Date repeatStart;
	public Date repeatEnd;

	public Boolean repeatMon;
	public Boolean repeatTue;
	public Boolean repeatWed;
	public Boolean repeatThur;
	public Boolean repeatFri;
	public Boolean repeatSat;
	public Boolean repeatSun;

	public Date eventStart;
	public Date eventEnd;
	public String title;
	public String info;
	public Long size;

	@ManyToOne
	public Avatar manager;

	@ManyToOne
	public EventType type;

	@OneToMany
	public List<Event> events;

	public EventRepeatable(Date repeatStart, Date repeatEnd, Boolean repeatMon, Boolean repeatTue, Boolean repeatWed, Boolean repeatThur, Boolean repeatFri, Boolean repeatSat, Boolean repeatSun, Date eventStart, Date eventEnd, String title, String info, Avatar manager, Long size) {

		this.repeatStart = repeatStart;
		this.repeatEnd = repeatEnd;
		this.repeatMon = repeatMon;
		this.repeatTue = repeatTue;
		this.repeatWed = repeatWed;
		this.repeatThur = repeatThur;
		this.repeatFri = repeatFri;
		this.repeatSat = repeatSat;
		this.repeatSun = repeatSun;

		this.eventStart = eventStart;
		this.eventEnd = eventEnd;
		this.title = title;
		this.info = info;
		this.manager = manager;
		this.size = size;

		this.save();

	}

}
