package models.wowapi.resources;

import play.*;
import play.data.validation.Required;
import play.db.DB;
import play.db.jpa.*;

import javax.persistence.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import models.wowapi.Armory;
import models.wowapi.character.Avatar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Entity
public class Realm extends Model {

	@Required
	public String name;

	@Required
	public String region;
	public Date lastUpdate;

	public String type;
	public String population;
	public Boolean queue;
	public Boolean status;
	public String slug;
	public String battlegroup;

	public Realm(String name, String region) {
		this.name = name;
		this.region = region;
		this.lastUpdate = new Date();
	}

	public Realm() {
		this.lastUpdate = new Date();
	}

	public static Realm getGuilRealm() {
		Realm guildRealm = Realm.findByName(Play.configuration.getProperty("guild.server"));
		if (guildRealm == null) {
			guildRealm = new Realm(Play.configuration.getProperty("guild.server"), Play.configuration.getProperty("wowapi.realmArea"));
			guildRealm.save();
			Logger.info("[Realm][getGuilRealm] Guild Realm created");
		}
		return guildRealm;
	}

	public static Realm findByName(String name) {
		try {
			PreparedStatement ps = DB.getConnection().prepareStatement("select id from Realm where BINARY name = ? and region = ?");
			ps.setString(1, name);
			ps.setString(2, Play.configuration.getProperty("wowapi.realmArea"));
			ResultSet rs = ps.executeQuery();

			if (rs.first()) {
				Logger.info("[Realm][findByName] Realm " + name + " found");
				return Realm.findById(rs.getLong("id"));
			} else {
				Logger.info("[Realm][findByName] Realm " + name + " not found");
				return null;
			}
			
			
		} catch (SQLException e) {
			Logger.warn("[Realm][findByName] " + e.getLocalizedMessage(), e);
			return null;
		}
	}

	public static void fetchRealms() {
		JsonArray realms = Armory.fetchFromArmory(Armory.REALMSURL).getAsJsonObject().get("realms").getAsJsonArray();

		for (JsonElement jsonElement : realms) {
			String type = jsonElement.getAsJsonObject().get("type").getAsString();
			String population = jsonElement.getAsJsonObject().get("population").getAsString();
			Boolean queue = jsonElement.getAsJsonObject().get("queue").getAsBoolean();
			Boolean status = jsonElement.getAsJsonObject().get("status").getAsBoolean();
			String name = jsonElement.getAsJsonObject().get("name").getAsString();
			String slug = jsonElement.getAsJsonObject().get("slug").getAsString();
			String battlegroup = jsonElement.getAsJsonObject().get("battlegroup").getAsString();

			Realm realm = Realm.findByName(name);

			if (realm != null) {
				realm.type = type;
				realm.population = population;
				realm.queue = queue;
				realm.status = status;
				realm.name = name;
				realm.slug = slug;
				realm.battlegroup = battlegroup;
				realm.lastUpdate = new Date();
				realm.region = Play.configuration.getProperty("wowapi.realmArea");
				realm.save();
			} else {
				realm = new Realm();
				realm.type = type;
				realm.population = population;
				realm.queue = queue;
				realm.status = status;
				realm.name = name;
				realm.slug = slug;
				realm.battlegroup = battlegroup;
				realm.lastUpdate = new Date();
				realm.region = Play.configuration.getProperty("wowapi.realmArea");
				realm.save();
			}
			Logger.info("[Realm][fetchRealms] " + realm);
		}
	}
	
	
	public String toString() {
		return name + " (" + region + ")";
	}

}
