package models.wowapi.guild;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class GuildAchievement extends Model {

	public Long aId;
	public Date timestamp;
	public Long criteria;
	public Long criteriaQuantity;
	public Date criteriaTimestamp;
	public Date criteriaCreated;

	public GuildAchievement(Long aId, Date timestamp, Long criteria, Long criteriaQuantity, Date criteriaTimestamp, Date criteriaCreated) {
		this.aId = aId;
		this.timestamp = timestamp;
		this.criteria = criteria;
		this.criteriaQuantity = criteriaQuantity;
		this.criteriaTimestamp = criteriaTimestamp;
		this.criteriaCreated = criteriaCreated;
	}
}
