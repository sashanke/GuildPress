package models.wowapi.logs;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;
@Entity
public class LogBoss extends Model {
	@ManyToOne
	public Logs log;
	public Long bossId;
	public String difficulty;
}
