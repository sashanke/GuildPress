package models.wowapi.guild;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import models.wowapi.Armory;
import models.wowapi.character.Avatar;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.Gender;
import models.wowapi.resources.GuildRank;
import models.wowapi.resources.Realm;
import play.Logger;
import play.db.DB;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class GuildMember extends Model {

	public String name;
	public String hash;
	public String image;
	public Long level;
	public Date lastUpdate;
	public Date lastModified;
	public Date created;
	
	@OneToOne
	public Avatar avatar;
	@ManyToOne
	public Guild guild;
	@ManyToOne
	public GuildRank rank;
	@ManyToOne
	public Realm realm;
	@ManyToOne
	public CharacterClass cclass;
	@ManyToOne
	public CharacterRace race;
	@ManyToOne
	public Gender gender;

	public GuildMember(String name, Guild guild) {
		this.name = name;
		this.hash = Codec.hexMD5(name); //27f9435463a2b883194b7fc8c9095148
		this.guild = guild;
		this.lastUpdate = new Date();
		this.lastModified = new Date(); 
		this.created = new Date();
	}

	public static Long fetchGuildMembers(Guild guild, JsonObject guildJson) {
		Logger.info("[GuildMember][fetchGuildMembers] " + guild.name + " (" + guild.realm + ")");
		JsonArray guildMembersJson = guildJson.get("members").getAsJsonArray();

		List<String> currentGuildMember = GuildMember.currentGuildMember();
		for (JsonElement guildMemberJson : guildMembersJson) {

			Long rank = guildMemberJson.getAsJsonObject().get("rank").getAsLong();
			GuildRank gr = GuildRank.find("rank = ?", rank).first();
			if (gr == null) {
				gr = new GuildRank(rank);
				gr.save();
			}

			JsonObject character = guildMemberJson.getAsJsonObject().get("character").getAsJsonObject();
			String gmname = character.get("name").getAsString();
			String gmhash = Codec.hexMD5(gmname);
			CharacterClass gmclass = CharacterClass.find("ccId", character.get("class").getAsLong()).first();
			CharacterRace gmrace = CharacterRace.find("crId", character.get("race").getAsLong()).first();
			Gender gmgender = Gender.find("gId", character.get("gender").getAsLong()).first();
			Long gmlevel = character.get("level").getAsLong();
			GuildRank gmrank = GuildRank.find("rank", gr.rank).first();
			String gmthumbnail = character.get("thumbnail").getAsString();

			currentGuildMember.remove(gmname);

			GuildMember gm = GuildMember.findByNameAndRealm(gmname, guild.realm);

			if (gm == null) {
				gm = new GuildMember(gmname, guild);
			}

			Logger.info("[GuildMember][fetchGuildMembers] " + gm.name + " " + guild.name + " (" + guild.realm + ")");
			gm.guild = guild;
			gm.name = gmname;
			gm.hash = gmhash;
			gm.realm = guild.realm;
			gm.cclass = gmclass;
			gm.race = gmrace;
			gm.gender = gmgender;
			gm.level = gmlevel;
			gm.rank = gmrank;
			gm.image = Armory.fetchAvatar(gm.realm, gm.name, gmthumbnail);
			gm.lastUpdate = new Date();
			gm.lastModified = new Date();
			gm.save();
			gm.avatar = Avatar.createAvatar(gm.name, gm.realm.name);
			gm.save();
		}

		// TODO: Remove old Guildmembers
		Collections.sort(currentGuildMember);

		return GuildMember.count();
	}
	
	/**
	 * Finds a guild member by name and realm, Binary Safe!
	 * 
	 * @param name
	 * @param realm
	 * @return GuildMember
	 */
	public static GuildMember findByNameAndRealm(String name, String realm) {
		return findByNameAndRealm(name, Realm.findByName(realm));
	}
	
	/**
	 * Finds a guild member by name and realm, Binary Safe!
	 * 
	 * @param name
	 * @param realm
	 * @return GuildMember
	 */
	public static GuildMember findByNameAndRealm(String name, Realm realm) {
		return GuildMember.find("hash = ?", Codec.hexMD5(name)).first();
//		try {
//			PreparedStatement ps = DB.getConnection().prepareStatement("select id from GuildMember where BINARY name = ? and BINARY realm_id = ?");
//			ps.setString(1, name);
//			ps.setLong(2, realm.id);
//			ResultSet rs = ps.executeQuery();
//			if (rs.first()) {
//				Logger.info("[GuildMember][findByNameAndRealm] GuildMember " + name + " found");
//				return GuildMember.findById(rs.getLong("id"));
//			} else {
//				Logger.info("[GuildMember][findByNameAndRealm] GuildMember " + name + " not found");
//				return null;
//			}
//		} catch (SQLException e) {
//			Logger.warn("[GuildMember][FindByNameAndRealm] " + e.getLocalizedMessage(), e);
//			return null;
//		}
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
}
