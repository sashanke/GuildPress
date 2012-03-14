package models.wowapi.guild;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.wowapi.character.WoWCharacter;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.Gender;
import models.wowapi.resources.GuildRank;
import play.db.jpa.Model;

@Entity
public class GuildMember extends Model {

	public String name;
	public String realm;
	@ManyToOne
	public CharacterClass cclass;
	@ManyToOne
	public CharacterRace race;
	@ManyToOne
	public Gender gender;
	public Long level;
	@ManyToOne
	public GuildRank rank;
	public Long achievementPoints;
	public String thumbnail;
	
	@OneToOne
	public WoWCharacter wowcharacter;
	
	public Boolean hasWoWCharacter;
	
	public Date lastUpdate;
	
	public GuildMember(String name, String realm, CharacterClass cclass, CharacterRace race, Gender gender, Long level, GuildRank rank, Long achievementPoints, String thumbnail) {
		this.name = name;
		this.realm = realm;
		this.cclass = cclass;
		this.race = race;
		this.gender = gender;
		this.level = level;
		this.rank = rank;
		this.achievementPoints = achievementPoints;
		this.thumbnail = thumbnail;
		this.lastUpdate = new Date();
	}

}
