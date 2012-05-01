package models.planner;

import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import org.joda.time.MutableDateTime;

public class PlannerWeek {
	public Date start;
	public Date end;
	public int week;
	LinkedHashMap<Integer, PlannerDay> days = new LinkedHashMap<Integer, PlannerDay>();
	public PlannerWeek(int week) {
		this.week = week;
		MutableDateTime startDate = new MutableDateTime();
		startDate.setWeekOfWeekyear(week);
		startDate.setDayOfWeek(1);
		this.start = startDate.toDate();

		MutableDateTime endDate = startDate;
		endDate.addDays(7);
		this.end = endDate.toDate();
		
		for (int i = 1; i <= 7; i++) {
			startDate.setDayOfWeek(i);			
			days.put(i, new PlannerDay(startDate.toDate()));
		}
		
	}
	
}
