package models.wowapi;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;
@Entity
public class LogZone extends Model {
	@ManyToOne
	Logs log;
	Long typeId;
    Long playerLimit;
    Long zoneId;
    String name;
    String difficulty;
}
