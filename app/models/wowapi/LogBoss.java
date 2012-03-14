package models.wowapi;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;
@Entity
public class LogBoss extends Model {
	@ManyToOne
	Logs log;
	Long bossId;
	String difficulty;
}
