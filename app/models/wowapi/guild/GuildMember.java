package models.wowapi.guild;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.wowapi.character.Avatar;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.Gender;
import models.wowapi.resources.GuildRank;
import models.wowapi.resources.Realm;
import play.Logger;
import play.db.DB;
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
	public Avatar wowcharacter;

	public Boolean hasWoWCharacter;

	public Date lastUpdate;
	public String avatar;

	public String getAvatar() {
		if (this.avatar == null || this.avatar.contains("noavatar.png")) {
			return "/public/images/static/avatar/" + this.race.crId + "-" + this.gender.gId + ".jpg";
		}
		if (this.avatar.startsWith(".")) {
			return this.avatar.substring(1);
		}
		return this.avatar;
	}

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

	public static List<String> currentGuildMember() {
		List<String> guildMember = new ArrayList<String>();
		try {
			PreparedStatement ps = DB.getConnection().prepareStatement("select name from GuildMember");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				guildMember.add(rs.getString("name"));
			}
			return guildMember;
		} catch (SQLException e) {
			Logger.info("Keinen passenden GuildMember in der Datenbank gefunden", e);
			return null;
		}
	}

	public static GuildMember findByNameAndRealm(String name, String realm) {
		try {
			PreparedStatement ps = DB.getConnection().prepareStatement("select id from GuildMember where BINARY name = ? and BINARY realm = ?");
			ps.setString(1, name);
			ps.setString(2, realm);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return GuildMember.findById(rs.getLong("id"));
		} catch (SQLException e) {
			Logger.info("Keinen passenden GuildMember zu (" + name + ") in der Datenbank gefunden", e);
			return null;
		}

	}
}
