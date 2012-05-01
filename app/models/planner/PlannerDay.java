package models.planner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Event;

public class PlannerDay {
	
	public Date date;
	
	public List<Event> events = new ArrayList<Event>();
	
	public PlannerDay(Date date) {
		this.date = date;
		this.events = Event.find("DATE(eventStart) = ?", date).fetch(4);
	}
}
