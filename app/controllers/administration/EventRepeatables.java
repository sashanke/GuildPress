package controllers.administration;

import java.util.Date;

import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableInterval;

import play.db.jpa.JPABase;
import models.Config;
import models.Event;
import models.EventRepeatable;
import models.wowapi.character.Avatar;
import controllers.CRUD;
import controllers.Check;

@Check("admin")
@CRUD.For(EventRepeatable.class)
public class EventRepeatables extends CRUD {


	public static void save(Long eventRepeatable, EventRepeatable object) throws Exception {
		
		EventRepeatable event = EventRepeatable.findById(eventRepeatable);
		
		event.repeatStart = object.repeatStart;
		event.repeatEnd = object.repeatEnd;
		event.repeatMon = object.repeatMon;
		event.repeatTue = object.repeatTue;
		event.repeatWed = object.repeatWed;
		event.repeatThur = object.repeatThur;
		event.repeatFri = object.repeatFri;
		event.repeatSat = object.repeatSat;
		event.repeatSun = object.repeatSun;

		event.eventStart = object.eventStart;
		event.eventEnd = object.eventEnd;
		event.title = object.title;
		event.info = object.info;
		event.manager = object.manager.save();
		event.size = object.size;

		event.save();

		if (event.events.size() > 0) {
			event.events.clear();
			event.save();
			Event.delete("repeatable = ?", event);
		}

		
		MutableDateTime mtd = new MutableDateTime(event.repeatStart);
		MutableInterval interval = new MutableInterval(event.repeatStart.getTime(), event.repeatEnd.getTime());
		for (int i = 0; i < interval.toDuration().getStandardDays(); i++) {

			if (event.repeatMon && mtd.getDayOfWeek() == DateTimeConstants.MONDAY && interval.contains(mtd.getMillis())) {
				createEvent(event, mtd);
			}
			if (event.repeatTue && mtd.getDayOfWeek() == DateTimeConstants.TUESDAY && interval.contains(mtd.getMillis())) {
				createEvent(event, mtd);
			}
			if (event.repeatWed && mtd.getDayOfWeek() == DateTimeConstants.WEDNESDAY && interval.contains(mtd.getMillis())) {
				createEvent(event, mtd);
			}
			if (event.repeatThur && mtd.getDayOfWeek() == DateTimeConstants.THURSDAY && interval.contains(mtd.getMillis())) {
				createEvent(event, mtd);
			}
			if (event.repeatFri && mtd.getDayOfWeek() == DateTimeConstants.FRIDAY && interval.contains(mtd.getMillis())) {
				createEvent(event, mtd);
			}
			if (event.repeatSat && mtd.getDayOfWeek() == DateTimeConstants.SATURDAY && interval.contains(mtd.getMillis())) {
				createEvent(event, mtd);
			}
			if (event.repeatSun && mtd.getDayOfWeek() == DateTimeConstants.SUNDAY && interval.contains(mtd.getMillis())) {
				createEvent(event, mtd);
			}

			mtd.addDays(1);
		}

		redirect(request.controller + ".list");
		
	}
	
	private static void createEvent(EventRepeatable eventRepeatable, MutableDateTime mtd) {

		MutableDateTime mtdStart = new MutableDateTime(eventRepeatable.eventStart);
		mtdStart.setDate(mtd.getMillis());
		Date eventStart = mtdStart.toDate();
		MutableDateTime mtdEnd = new MutableDateTime(eventRepeatable.eventEnd);
		mtdEnd.setDate(mtd.getMillis());
		Date eventEnd = mtdEnd.toDate();
		eventRepeatable.save();
		Event event = new Event(eventStart, eventEnd, eventRepeatable.title, eventRepeatable.info, eventRepeatable.manager, eventRepeatable.size);
		event.type = eventRepeatable.type;
		event.repeatable = eventRepeatable;
		event.save();
		eventRepeatable.events.add(event);
		eventRepeatable.save();
	}
	
	
}