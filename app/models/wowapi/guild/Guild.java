package models.wowapi.guild;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import models.Comment;
import models.wowapi.resources.Realm;
import models.wowapi.resources.Side;

import java.util.*;

@Entity
public class Guild extends Model {

	@Required
	public String name;
	@Required
	@ManyToOne
	public Realm realm;
	@Required
	public Long level;
	
	@Required
	public Long members;
	
	@Required
	public Long achievementPoints;
	@Required
	public Date lastModified;
	@ManyToOne
	public Side side;
	@ManyToOne(cascade=CascadeType.ALL)
	public GuildEmblem emblem;
	
	public Boolean isMainGuild;
	
	public Date lastUpdate;
	public Long score;
	public Long world_rank;
	public Long area_rank;
	public Long realm_rank;
	
	
	public Guild(Date lastModified, String name, Realm realm, Long level, Side side, Long achievementPoints, GuildEmblem emblem) {
		this.lastModified = lastModified;
		this.name = name;
		this.realm = realm;
		this.level = level;
		this.side = side;
		this.achievementPoints = achievementPoints;
		this.emblem = emblem;
		this.lastUpdate = new Date();
	}
	
	public static Guild getMainGuild() {
		return find("byIsMainGuild", true).first();
	}
	
	public String toString() {
		return name + " (" + realm + ")";
	}

}
