package models.recruitment;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;
import utils.Tools;
@Entity
public class RaidTime extends Model {
	
	String day;
	
	Long start;
	
	Long end;
	
	Long sort;
	
	@ManyToOne
	public Recruitment recruitment;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return day + " (" + Tools.long2raidTime(start) + " - " + Tools.long2raidTime(end) + ")";
	}
}
