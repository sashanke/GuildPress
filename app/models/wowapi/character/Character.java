package models.wowapi.character;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.wowapi.guild.Guild;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.Gender;
import models.wowapi.resources.GuildRank;
import models.wowapi.resources.Realm;
import play.db.jpa.Model;

@Entity
public class Character extends Model {

	public String name;
	@ManyToOne
	public Realm realm;
	@ManyToOne
	public CharacterClass cclass;
	@ManyToOne
	public CharacterRace race;
	@ManyToOne
	public Gender gender;
	public Long level;
	public Long achievementPoints;
	public String thumbnail;
	public String avatar;
	@ManyToOne
	public Guild guild;
	
	public Boolean isGuildMember;
	
	@OneToOne
	public GuildMember guildMember;
	
	public Date lastUpdate;
	public Date lastModified;
	
	public Character() {
		this.lastUpdate = new Date();
	}

}
