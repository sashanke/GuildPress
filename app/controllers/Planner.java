package controllers;

import java.util.Date;

import models.Event;
import models.EventAttendee;
import models.User;
import models.planner.PlannerMonth;
import models.types.Confirmation;

import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.GregorianChronology;

import play.mvc.Before;
import play.mvc.Controller;

public class Planner extends Controller {
	@Before(unless={"event"})
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void index() {
		DateTime now = new DateTime();
		int year = now.year().get();
		int month = now.monthOfYear().get();
		calendar(year, month);
	}
	
	public static void previus(int year, int month) {
		MutableDateTime mdt = new MutableDateTime();
		mdt.setYear(year);
		mdt.setMonthOfYear(month);
		mdt.addMonths(-1);
		year = mdt.year().get();
		month = mdt.monthOfYear().get();
		calendar(year, month);
	}
	
	public static void next(int year, int month) {
		MutableDateTime mdt = new MutableDateTime();
		mdt.setYear(year);
		mdt.setMonthOfYear(month);
		mdt.addMonths(+1);
		year = mdt.year().get();
		month = mdt.monthOfYear().get();
		calendar(year, month);
	}
	
	public static void calendar(int year, int month) {
		PlannerMonth displayMonth = new PlannerMonth(year, month);
		render(displayMonth);
	}
	
	public static void event(Long id) {
		Event event = Event.findById(id);
		render(event);
	}
	
	public static void signEvent(Long id) {
		Event event = Event.findById(id);
		
		User user = User.getConnectedUser(session.get("username"));
		if (user != null) {
			EventAttendee eventAttendee = EventAttendee.find("avatar = ? and event = ?", user.avatar,event).first();
			if (eventAttendee == null) {
				event.attendees.add(new EventAttendee(Confirmation.SIGNED, user.avatar, new Date(), event));
				event.save();
			} else {
				event.attendees.remove(eventAttendee);
				eventAttendee.confirmation = Confirmation.SIGNED;
				eventAttendee.save();
				event.attendees.add(eventAttendee);
				event.save();
			}
		}
		calendar(event.eventStart.getYear()+1900, event.eventStart.getMonth()+1);
	}
	
	public static void unsignEvent(Long id) {
		Event event = Event.findById(id);
		
		User user = User.getConnectedUser(session.get("username"));
		if (user != null) {
			EventAttendee eventAttendee = EventAttendee.find("avatar = ? and event = ?", user.avatar,event).first();
			if (eventAttendee == null) {
				event.attendees.add(new EventAttendee(Confirmation.DEREGISTERED, user.avatar, new Date(), event));
				event.save();
			} else {
				event.attendees.remove(eventAttendee);
				eventAttendee.confirmation = Confirmation.DEREGISTERED;
				eventAttendee.save();
				event.attendees.add(eventAttendee);
				event.save();
			}
		}
		
		calendar(event.eventStart.getYear()+1900, event.eventStart.getMonth()+1);
	}
	
	public static void signTentativeEvent(Long id) {
		Event event = Event.findById(id);
		
		User user = User.getConnectedUser(session.get("username"));
		if (user != null) {
			EventAttendee eventAttendee = EventAttendee.find("avatar = ? and event = ?", user.avatar,event).first();
			if (eventAttendee == null) {
				event.attendees.add(new EventAttendee(Confirmation.TENTATIVE, user.avatar, new Date(), event));
				event.save();
			} else {
				event.attendees.remove(eventAttendee);
				eventAttendee.confirmation = Confirmation.TENTATIVE;
				eventAttendee.save();
				event.attendees.add(eventAttendee);
				event.save();
			}
		}
		calendar(event.eventStart.getYear()+1900, event.eventStart.getMonth()+1);
	}
	
}
