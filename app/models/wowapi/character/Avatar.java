package models.wowapi.character;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Comment;
import models.News;
import models.wowapi.Armory;
import models.wowapi.guild.Guild;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.Gender;
import models.wowapi.resources.GuildRank;
import models.wowapi.resources.Realm;
import models.wowapi.resources.Side;
import play.Logger;
import play.Play;
import play.db.jpa.Model;

@Entity
public class Avatar extends Model {

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
	//http://eu.battle.net/wow/static/images/character/summary/backgrounds/race/1.jpg
	public String profile; //63010918-profilemain.jpg?alt=/wow/static/images/2d/profilemain/race/1-1.jpg
	
	@ManyToOne
	public Guild guild;
	
	public Boolean isGuildMember;
	
	@OneToOne
	public GuildMember guildMember;
	
	public Date lastUpdate;
	public Date lastModified;
	
	@OneToMany(mappedBy="avatar", cascade=CascadeType.ALL)
	public List<AvatarItem> items;
	
	public Long averageItemLevel;
	public Long averageItemLevelEquipped;
	public String inset;
	
	
	public Avatar() {
		this.lastUpdate = new Date();
		this.items = new ArrayList<AvatarItem>();
	}
	
	public Avatar addItem(AvatarItem item) {
		item.save();
        this.items.add(item);
        this.save();
        return this;
    }
	
	
	public String getAvatarBanner() {
		String banner = Play.configuration.getProperty("conf.bannerdir") + this.race.side.name.toLowerCase() + "/" + this.race.name.toLowerCase() + "/" + this.race.name.toLowerCase() + "_" + this.cclass.name.toLowerCase() + "_" + this.gender.name_loc.toLowerCase() + ".jpg";
		return banner;
	}
	public String getProfileMain() {
		String bg = "/public/images/static/profilemain/" + this.race.crId + "-" + this.gender.gId + ".jpg";
		bg = "/public/profiles/" + this.race.crId + "-" + this.gender.gId + ".jpg";
		
		return this.profile;
	}
}
