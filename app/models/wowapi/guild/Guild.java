package models.wowapi.guild;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.wowapi.Armory;
import models.wowapi.resources.Realm;
import models.wowapi.resources.Side;
import play.Logger;
import play.Play;
import play.db.DB;
import play.db.jpa.Model;
import play.libs.Codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 * Basis Model für die Gildenendaten
 * 
 * Basiert auf den Json Daten des Blizzard Armory
 * http://eu.battle.net/api/wow/guild
 * /Anub%27arak/Novus%20Ordo%20Mundi?locale=de_DE&fields
 * 
 * WoWProgress Rankings
 * http://www.wowprogress.com/guild/eu/anub-arak/Novus%20Ordo%20Mundi/json_rank
 * 
 * Armory fields die bisher ausgewerted werden: achievements members
 * 
 * @author Calenria
 * 
 */
@Entity
public class Guild extends Model {

	public static boolean checkMainGuild(String name, Realm realm) {
		if (name.equals(Play.configuration.getProperty("wowapi.guildName")) && realm.name.equals(Play.configuration.getProperty("wowapi.realmName"))) {
			return true;
		}
		return false;
	}

	/**
	 * Creates a Guild
	 * 
	 * @param guild
	 * 
	 * @return Guild
	 */
	private static Guild createGuild(Guild guild) {
		Logger.info("[Guild][createGuild] " + guild.name + " (" + guild.realm + ")");
		return fetchGuild(guild);
	}

	/**
	 * Creates a Guild
	 * 
	 * @param name
	 * @param realm
	 * @return Guild
	 */
	public static Guild createGuild(String name, String realm) {
		Guild guild = Guild.findByNameAndRealm(name, realm);
		if (guild == null) {
			return createGuild(new Guild(name, realm));
		} else {
			return updateGuild(guild);
		}
	}

	/**
	 * Fetch an specific Guild from the WoW Armory
	 * 
	 * @param name
	 * @param realm
	 * @return Guild
	 */
	public static Guild fetchGuild(Guild guild) {
		Logger.info("[Guild][fetchGuild] " + guild.name + " (" + guild.realm + ")");
		JsonObject guildJson = Armory.getGuildJson(guild.name, guild.realm);
		guild.lastModified = new Date(guildJson.get("lastModified").getAsLong());
		guild.name = guildJson.get("name").getAsString();
		guild.hash = Codec.hexMD5(guild.name);
		guild.level = guildJson.get("level").getAsLong();
		guild.side = Side.find("sId", guildJson.get("side").getAsLong()).first();
		guild.achievementPoints = guildJson.get("achievementPoints").getAsLong();
		guild.isMainGuild = checkMainGuild(guild.name, guild.realm);
		guild.lastUpdate = new Date();
		guild.save();
		if (checkMainGuild(guild.name, guild.realm)) {
			guild.members = setGuildMembers(guild, guildJson);
			setGuildAchievements(guild, guildJson);
		}
		guild.emblem = setGuildEmblem(guild, guildJson);
		guild.save();

		setGuildRank(guild);
		return guild;
	}

	/**
	 * Fetch an specific Guild from the WoW Armory
	 * 
	 * @param name
	 * @param realm
	 * @return Guild
	 */
	public static Guild fetchGuild(String name, String realm) {
		return fetchGuild(Guild.findByNameAndRealm(name, realm));
	}

	/**
	 * Finds a guild by name and realm, Binary Safe!
	 * 
	 * @param name
	 * @param realm
	 * @return Guild
	 */
	public static Guild findByNameAndRealm(String name, Realm realm) {
		try {
			PreparedStatement ps = DB.getConnection().prepareStatement("select id from Guild where BINARY name = ? and BINARY realm_id = ?");
			ps.setString(1, name);
			ps.setLong(2, realm.id);
			ResultSet rs = ps.executeQuery();
			rs.next();

			if (rs.first()) {
				Logger.info("[Guild][findByNameAndRealm] Guild " + name + " found");
				return Guild.findById(rs.getLong("id"));
			} else {
				Logger.info("[Guild][findByNameAndRealm] Guild " + name + " not found");
				return null;
			}

		} catch (SQLException e) {
			Logger.warn("[Guild][FindByNameAndRealm] " + e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 * Finds a guild by name and realm, Binary Safe!
	 * 
	 * @param name
	 * @param realm
	 * @return Guild
	 */
	public static Guild findByNameAndRealm(String name, String realm) {
		return findByNameAndRealm(name, Realm.findByName(realm));
	}

	public static Guild getMainGuild() {
		return find("byIsMainGuild", true).first();
	}

	/**
	 * Sets the Achievements for an Guild
	 * 
	 * @param guild
	 * @param guildJson
	 */
	public static void setGuildAchievements(Guild guild, JsonObject guildJson) {
		Logger.info("[Guild][setGuildAchievements] " + guild.name + " (" + guild.realm + ")");
		JsonObject achievements = guildJson.get("achievements").getAsJsonObject();
		JsonArray achievementsCompleted = achievements.get("achievementsCompleted").getAsJsonArray();
		JsonArray achievementsCompletedTimestamp = achievements.get("achievementsCompletedTimestamp").getAsJsonArray();
		JsonArray criteria = achievements.get("criteria").getAsJsonArray();
		JsonArray criteriaQuantity = achievements.get("criteriaQuantity").getAsJsonArray();
		JsonArray criteriaTimestamp = achievements.get("criteriaTimestamp").getAsJsonArray();
		JsonArray criteriaCreated = achievements.get("criteriaCreated").getAsJsonArray();

		int count = 0;
		for (@SuppressWarnings("unused")
		JsonElement fachievementsCompleted : achievementsCompleted) {
			Long laId = achievementsCompleted.get(count).getAsLong();
			Date dtimestamp = new Date(achievementsCompletedTimestamp.get(count).getAsLong());
			Long lcriteria = criteria.get(count).getAsLong();
			Long lcriteriaQuantity = criteriaQuantity.get(count).getAsLong();
			Date dcriteriaTimestamp = new Date(criteriaTimestamp.get(count).getAsLong());
			Date dcriteriaCreated = new Date(criteriaCreated.get(count).getAsLong());

			GuildAchievement ga = GuildAchievement.find("aId = ? and guild = ?", laId, guild).first();
			if (ga != null) {
				ga.aId = laId;
				ga.timestamp = dtimestamp;
				ga.criteria = lcriteria;
				ga.criteriaQuantity = lcriteriaQuantity;
				ga.criteriaTimestamp = dcriteriaTimestamp;
				ga.criteriaCreated = dcriteriaCreated;
				ga.guild = guild;
				ga.save();
			} else {
				ga = new GuildAchievement(laId, dtimestamp, lcriteria, lcriteriaQuantity, dcriteriaTimestamp, dcriteriaCreated, guild);
				ga.save();
			}
			count++;
		}
	}

	/**
	 * Sets the Guildemblem for an Guild
	 * 
	 * @param guild
	 * @param guildJson
	 * @return GuildEmblem
	 */
	private static GuildEmblem setGuildEmblem(Guild guild, JsonObject guildJson) {
		Logger.info("[Guild][setGuildEmblem] " + guild.name + " (" + guild.realm + ")");
		JsonObject guildEmblemJson = guildJson.get("emblem").getAsJsonObject();

		GuildEmblem guildEmblem = GuildEmblem.find("guild = ?", guild).first();
		if (guildEmblem != null) {
			guildEmblem.guild = guild;
			guildEmblem.icon = guildEmblemJson.get("icon").getAsLong();
			guildEmblem.iconColor = guildEmblemJson.get("iconColor").getAsString();
			guildEmblem.border = guildEmblemJson.get("border").getAsLong();
			guildEmblem.borderColor = guildEmblemJson.get("borderColor").getAsString();
			guildEmblem.backgroundColor = guildEmblemJson.get("backgroundColor").getAsString();
		} else {
			guildEmblem = new GuildEmblem();
			guildEmblem.guild = guild;
			guildEmblem.icon = guildEmblemJson.get("icon").getAsLong();
			guildEmblem.iconColor = guildEmblemJson.get("iconColor").getAsString();
			guildEmblem.border = guildEmblemJson.get("border").getAsLong();
			guildEmblem.borderColor = guildEmblemJson.get("borderColor").getAsString();
			guildEmblem.backgroundColor = guildEmblemJson.get("backgroundColor").getAsString();
			guildEmblem.save();
		}
		return guildEmblem;
	}

	private static Long setGuildMembers(Guild guild, JsonObject guildJson) {
		return GuildMember.fetchGuildMembers(guild, guildJson);
	}

	/**
	 * Sets the World/Area/Realm Rank
	 * 
	 * @param guild
	 */
	private static void setGuildRank(Guild guild) {
		Logger.info("[Guild][setGuildRank] " + guild.name + " (" + guild.realm + ")");
		JsonElement guildRankingsJson = Armory.getGuildRankJson(guild);
		if (guildRankingsJson.isJsonObject()) {
			JsonObject guildRankings = guildRankingsJson.getAsJsonObject();
			guild.score = guildRankings.get("score").getAsLong();
			guild.world_rank = guildRankings.get("world_rank").getAsLong();
			guild.area_rank = guildRankings.get("area_rank").getAsLong();
			guild.realm_rank = guildRankings.get("realm_rank").getAsLong();
			guild.save();
		}
	}

	/**
	 * Update a Guild
	 * 
	 * @param guild
	 * 
	 * @return Guild
	 */
	public static Guild updateGuild(Guild guild) {
		if (Armory.checkUpdate(guild.lastUpdate, Armory.QUATERDAYUPDATE)) {
			Logger.info("[Guild][updateGuild] " + guild.name + " (" + guild.realm + ")");
			return fetchGuild(guild);
		}
		return guild;
	}

	public String name;

	public String hash;

	public Long level;

	public Long members;

	public Long achievementPoints;

	public Long score;

	public Long world_rank;

	public Long area_rank;

	public Long realm_rank;

	public Date lastModified;

	public Date lastUpdate;

	public Date created;

	public Boolean isMainGuild;

	@ManyToOne
	public Realm realm;

	@ManyToOne
	public Side side;

	@OneToOne(cascade = CascadeType.ALL)
	public GuildEmblem emblem;

	/**
	 * Creates a Guild
	 * 
	 * @param name
	 * @param realm
	 */
	public Guild(String name, String realm) {
		this.name = name;
		this.hash = Codec.hexMD5(name);
		this.realm = Realm.findByName(realm);
		this.created = new Date();
		this.lastUpdate = new Date();
		this.score = 0L;
		this.world_rank = 0L;
		this.area_rank = 0L;
		this.realm_rank = 0L;
		this.members = 0L;
	}

	@Override
	public String toString() {
		return this.name + " (" + this.realm + ")";
	}

}
