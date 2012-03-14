package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import models.Post;

import play.db.jpa.Model;

/**
 * http://eu.battle.net/api/wow/data/guild/perks?locale=de_DE
 * @author prime
 *
 */
@Entity
public class GuildPerkSpell extends Model {

	public Long gpsId;
	public String name;
	public String subtext;
	public String icon;
	@Lob
	public String description;
	
	@ManyToOne
    public GuildPerk guildperk;
	
	
	
	Boolean isSpell = true;
	public Date lastUpdate;
	public GuildPerkSpell(GuildPerk guildperk, Long gpsId, String name, String subtext, String icon, String description) {
		this.guildperk = guildperk;
		this.gpsId = gpsId;
		this.name = name;
		this.subtext = subtext;
		this.icon = icon;
		this.description = description;
		this.lastUpdate = new Date();
	}

}
