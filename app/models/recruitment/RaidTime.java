package models.recruitment;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;
@Entity
public class RaidTime extends Model {
	@ManyToOne
	public Recruitment recruitment;
}
