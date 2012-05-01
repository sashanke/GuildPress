package controllers;

import models.planner.PlannerMonth;

import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.GregorianChronology;

import play.mvc.Before;
import play.mvc.Controller;

public class Planner extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void index() {
		DateTime now = new DateTime();
		int year = now.year().get();
		int month = now.monthOfYear().get();
		calendar(year, month);
	}

	public static void calendar(int year, int month) {
		PlannerMonth displayMonth = new PlannerMonth(year, month);
		render(displayMonth);
	}
}
