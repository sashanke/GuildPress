package models.wowapi.logs;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class LogZone extends Model {
	@ManyToOne
	public Logs log;
	public Long typeId;
	public Long playerLimit;
	public Long zoneId;
	public String name;
	public String difficulty;
}
