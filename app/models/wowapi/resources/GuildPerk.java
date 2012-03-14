package models.wowapi.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import models.Comment;

import play.db.jpa.Model;

/**
 * http://eu.battle.net/api/wow/data/guild/perks?locale=de_DE
 * @author prime
 *
 */
@Entity
public class GuildPerk extends Model {

	public Long guildLevel;

	public Boolean isSpell = false;
	public Date lastUpdate;
	
	@OneToMany(mappedBy="guildperk", cascade=CascadeType.ALL)
    public List<GuildPerkSpell> spell;
	
	public GuildPerk(Long guildLevel) {	
		this.guildLevel = guildLevel;
		this.spell = new ArrayList<GuildPerkSpell>();
		this.lastUpdate = new Date();
	}
	
	public static GuildPerk getGuildPerk(Long level) {
		return find("byGuildLevel", level).first();
	}

}
