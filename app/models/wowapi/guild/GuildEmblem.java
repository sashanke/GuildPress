package models.wowapi.guild;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class GuildEmblem extends Model {
	@OneToOne
	public Guild guild;

	public String borderColor;

	public String iconColor;

	public String backgroundColor;

	public Long icon;

	public Long border;
}
