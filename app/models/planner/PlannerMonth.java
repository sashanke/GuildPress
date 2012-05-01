package models.planner;

import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

public class PlannerMonth {
	
	public Date start;
	public Date end;
	public int year;
	public int month;
	public int week;
	public Date date;
	
	LinkedHashMap<Integer, PlannerWeek> weeks = new LinkedHashMap<Integer, PlannerWeek>();
	
	public PlannerMonth(int year, int month) {
		this.year = year;
		this.month = month;
		
		DateTime date = new DateTime(this.year, this.month, 1, 12, 0, 0, 0);
		this.week = date.getWeekOfWeekyear();
		this.date = date.toDate();
		
		MutableDateTime startDate = new MutableDateTime();
		startDate.setWeekOfWeekyear(this.week);
		startDate.setDayOfWeek(1);
		this.start = startDate.toDate();

		MutableDateTime endDate = startDate;
		endDate.addWeeks(6);
		this.end = endDate.toDate();
		
		for (int i = this.week-1; i < (this.week+5); i++) {
			weeks.put(i, new PlannerWeek(i));
		}
		
	}
}
