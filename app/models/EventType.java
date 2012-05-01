package models;

import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class EventType extends Model {
	public String image;
	public String name;
	
	public Boolean raid;
	public Boolean guildevent;
	public Boolean wowevent;
	
	public EventType(String image, String name) {
		this.image = image;
		this.name = name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
}
