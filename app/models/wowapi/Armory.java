package models.wowapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import models.wowapi.character.Avatar;
import models.wowapi.guild.Guild;
import models.wowapi.guild.GuildAchievement;
import models.wowapi.guild.GuildEmblem;
import models.wowapi.guild.GuildMember;
import models.wowapi.logs.LogBoss;
import models.wowapi.logs.LogZone;
import models.wowapi.logs.Logs;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.Gender;
import models.wowapi.resources.GuildPerk;
import models.wowapi.resources.GuildPerkSpell;
import models.wowapi.resources.GuildRank;
import models.wowapi.resources.ItemClass;
import models.wowapi.resources.Realm;
import models.wowapi.resources.Side;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import flexjson.JSONDeserializer;

public class Armory {

	public static long WEEKLYUPDATE = 604800000;
	public static long QUATERDAYUPDATE = 21600000;

	public static String CHARACTERCLASSESURL = "/api/wow/data/character/classes";
	public static String CHARACTERRACESURL = "/api/wow/data/character/races";
	public static String GUILDPERKSURL = "/api/wow/data/guild/perks";
	public static String ITEMCLASSESURL = "/api/wow/data/item/classes";
	public static String GUILDURL = "/api/wow/guild/";
	public static String CHARURL = "/api/wow/character/";
	public static String REALMSURL = "/api/wow/realm/status";

	public static String WOWPROGRESSURL = "http://www.wowprogress.com/guild/";

	public Armory() {

		List<Side> sides = Side.findAll();
		if (sides.size() == 0) {
			new Side(new Long(0), "alliance").save();
			new Side(new Long(1), "horde").save();
		}
		List<Gender> genders = Gender.findAll();
		if (genders.size() == 0) {
			new Gender(new Long(0), "male").save();
			new Gender(new Long(1), "female").save();
		}

		setRealms();
		setCharacterClasses();
		setCharacterRaces();
		setGuildPerks();
		setItemClasses();
		setMainGuild();

		setLastLogs();

	}

	public static String fetchAvatar(String realm, String name, String thumbnail) {
		String dir = "." + File.separator + "public" + File.separator + "avatars" + File.separator + realm + File.separator;
		File avatarDir = new File(dir);
		String image = "." + File.separator + "public" + File.separator + "avatars" + File.separator + "noavatar.png";
		if (thumbnail != null) {
			Logger.info("Fetch Avatar: http://eu.battle.net/static-render/eu/" + thumbnail);
			WSRequest wsr = WS.url("http://eu.battle.net/static-render/eu/" + thumbnail + "");
			HttpResponse hr = wsr.get();
			if (hr.success()) {
				avatarDir.mkdirs();
				String contentType = hr.getContentType();
				if (contentType.contains("image")) {
					String avImage = dir + name + "." + contentType.substring(6);
					File avatar = new File(avImage);
					try {
						InputStream inputStream = hr.getStream();
						OutputStream out = new FileOutputStream(avatar);

						byte buf[] = new byte[1024];
						int len;
						while ((len = inputStream.read(buf)) > 0)
							out.write(buf, 0, len);
						out.close();
						inputStream.close();
						return castURL(avImage);
					} catch (FileNotFoundException e) {
						Logger.error(e, "Konnte datei " + avatar.getAbsolutePath() + " nicht finden!");
					} catch (IOException e) {
						Logger.error(e, "Es ist ein Fehler beim speichern von  " + avatar.getAbsolutePath() + " aufgetreten!");
					}
				} else {
					Logger.error("Konnte Kontenttyp  von " + thumbnail + " nicht bestimmen. (" + contentType + ")");
				}
			} else {
				Logger.error("Konnte Avatar von " + name + " nicht vom UpdateJob holen. (" + hr.getStatus() + ")");
			}
		}
		return castURL(image);
	}

	private static String castURL(String image) {
		image = image.replaceAll("\\.\\\\", "/");
		image = image.replaceAll("\\\\", "/");
		return image;
	}

	private void setLastLogs() {
		Logs log = Logs.find("order by lastUpdate desc").first();
		Boolean update = false;
		try {
			if (checkUpdate(new Date(), log.lastUpdate, Armory.QUATERDAYUPDATE)) {
				update = true;
			}
		} catch (NullPointerException e) {
			update = true;
		}
		if (update) {
			Logger.info("Fetching Logs");
			fetchLogs();
		}

	}

	private void fetchLogs() {
		// TODO Auto-generated method stub
		// WoLogs l = WoLogs.all().first();
		Logger.info("Fetch URL: " + Play.configuration.getProperty("wol.feedurl"));

		JsonElement je = WS.url(Play.configuration.getProperty("wol.feedurl")).get().getJson();
		JsonArray ja = je.getAsJsonObject().get("rows").getAsJsonArray();
		for (JsonElement jsonElement : ja) {

			String logId = jsonElement.getAsJsonObject().get("id").getAsString();

			Logs log = Logs.find("byLogId", logId).first();
			if (log == null) {
				log = new Logs();
				Date date = new Date(jsonElement.getAsJsonObject().get("date").getAsLong());
				Long healingDone = jsonElement.getAsJsonObject().get("healingDone").getAsLong();
				Long damageDone = jsonElement.getAsJsonObject().get("damageDone").getAsLong();
				Long wipeCount = jsonElement.getAsJsonObject().get("wipeCount").getAsLong();
				Long damageTaken = jsonElement.getAsJsonObject().get("damageTaken").getAsLong();
				Long bossCount = jsonElement.getAsJsonObject().get("bossCount").getAsLong();
				Long duration = jsonElement.getAsJsonObject().get("duration").getAsLong();
				Long killCount = jsonElement.getAsJsonObject().get("killCount").getAsLong();

				log.bossCount = bossCount;
				log.damageDone = damageDone;
				log.damageTaken = damageTaken;
				log.date = date;
				log.duration = duration;
				log.healingDone = healingDone;
				log.killCount = killCount;
				log.logId = logId;
				log.wipeCount = wipeCount;
				log.save();

				getZones(log, jsonElement);
				getBosses(log, jsonElement);

				ArrayList<Avatar> participants = new ArrayList<Avatar>();
				JsonArray jaParticipants = jsonElement.getAsJsonObject().get("participants").getAsJsonArray();
				for (JsonElement jaParticipant : jaParticipants) {
					String name = jaParticipant.getAsString();
					Avatar participant = Avatar.find("name = ? and realm.name = ?", name, Play.configuration.getProperty("wowapi.realmName")).first();
					if (participant == null) {
						participant = fetchCharacter(Play.configuration.getProperty("wowapi.realmName"), name);
					}
					participants.add(participant);
				}
				log.participants = participants;
			}
			log.lastUpdate = new Date();
			log.save();

		}
	}

	public void getBosses(Logs log, JsonElement jsonElement) {
		JsonArray jaBosses = jsonElement.getAsJsonObject().get("bosses").getAsJsonArray();
		for (JsonElement jsonElement2 : jaBosses) {
			Long bossId = jsonElement2.getAsJsonArray().get(0).getAsLong();
			String difficulty = jsonElement2.getAsJsonArray().get(1).getAsString();
			LogBoss boss = new LogBoss();
			boss.bossId = bossId;
			boss.difficulty = difficulty;
			boss.log = log;
			boss.save();

		}
	}

	public void getZones(Logs log, JsonElement jsonElement) {
		JsonArray jaZones = jsonElement.getAsJsonObject().get("zones").getAsJsonArray();
		for (JsonElement jsonElement2 : jaZones) {
			Long typeId = jsonElement2.getAsJsonObject().get("typeId").getAsLong();
			Long playerLimit = jsonElement2.getAsJsonObject().get("playerLimit").getAsLong();
			Long zoneId = jsonElement2.getAsJsonObject().get("id").getAsLong();
			String name = jsonElement2.getAsJsonObject().get("name").getAsString();
			String difficulty = jsonElement2.getAsJsonObject().get("difficulty").getAsString();
			LogZone zone = new LogZone();

			zone.typeId = typeId;
			zone.playerLimit = playerLimit;
			zone.zoneId = zoneId;
			zone.name = name;
			zone.difficulty = difficulty;
			zone.log = log;
			zone.save();
		}
	}

	public static Avatar fetchCharacter(String vRealm, String vName) {
		Avatar m = Avatar.find("name = ? and realm.name = ?", vName, vRealm).first();

		if (m != null && m.guild != null) {
			checkGuild(m.guild.name, m.guild.realm);
		}

		Boolean update = false;
		try {
			if (checkUpdate(new Date(), m.lastUpdate, Armory.QUATERDAYUPDATE)) {
				update = true;
			}
		} catch (NullPointerException e) {
			update = true;
		}
		if (update) {
			Logger.info("Fetching Character: " + vName + " - " + vRealm);
			return fetchCharacter(update, vRealm, vName);
		}
		return m;
	}

	public static Avatar fetchCharacter(Boolean update, String vRealm, String vName) {
		JsonElement armory = fetchFromArmory(Armory.CHARURL, "guild,stats,talents,items,professions,appearance,progression,pvp", vRealm, vName);		
		if (armory == null) {
			Avatar m = Avatar.find("name = ? and realm.name = ?", vName, vRealm).first();
			if (m != null) {
				Logger.info("UpdateJob Error/Down but Char was found in Database: " + m.name + " - " + m.realm.name);
				return m;
			}
			GuildMember gm = GuildMember.find("name = ? and realm = ?", vName, vRealm).first();
			if (gm != null && m == null) {
				Logger.info("Creating new Char from Guildmember: " + gm.name + " - " + gm.realm);
				m = new Avatar();
				m.achievementPoints = gm.achievementPoints;
				m.guild = Guild.getMainGuild();
				m.avatar = gm.avatar;
				m.cclass = gm.cclass;
				m.gender = gm.gender;
				m.lastUpdate = new Date();
				m.level = gm.level;
				m.name = gm.name;
				m.race = gm.race;
				m.realm = Realm.find("byName", gm.realm).first();
				m.thumbnail = gm.thumbnail;
				m.isGuildMember = true;
				m.guildMember = gm;
				m.lastModified = new Date(1L);
				m.save();
				gm.wowcharacter = m;
				gm.hasWoWCharacter = true;
				gm.save();
				return m;
			}

		} else {

			JsonObject character = armory.getAsJsonObject();

			String cRealm;
			String cGuildName;
			Long cGuildMembers;
			Guild g = null;
			if (character.has("guild")) {
				cRealm = character.getAsJsonObject("guild").get("realm").getAsString();
				cGuildName = character.getAsJsonObject("guild").get("name").getAsString();
				cGuildMembers = character.getAsJsonObject("guild").get("members").getAsLong();
				Realm realm = Realm.find("name = ?", cRealm).first();
				g = checkGuild(cGuildName, realm);
				g.members = cGuildMembers;
				g.save();
			}

			String gmname = character.get("name").getAsString();
			Realm gmrealm = Realm.find("name", character.get("realm").getAsString()).first();
			CharacterClass gmclass = CharacterClass.find("ccId", character.get("class").getAsLong()).first();
			CharacterRace gmrace = CharacterRace.find("crId", character.get("race").getAsLong()).first();
			Gender gmgender = Gender.find("gId", character.get("gender").getAsLong()).first();
			Long gmlevel = character.get("level").getAsLong();
			Long gmachievementPoints = character.get("achievementPoints").getAsLong();
			String gmthumbnail = character.get("thumbnail").getAsString();
			Long gmlastModified = character.get("lastModified").getAsLong();

			Avatar gm = Avatar.find("name = ? and realm.name = ?", gmname, gmrealm.name).first();
			GuildMember guildm = GuildMember.find("name = ? and realm = ?", gmname, gmrealm.name).first();
			Boolean isGuildMember = false;

			if (guildm != null) {
				isGuildMember = true;
			}

			if (gm != null) {
				gm.name = gmname;
				gm.realm = gmrealm;
				gm.cclass = gmclass;
				gm.race = gmrace;
				gm.gender = gmgender;
				gm.level = gmlevel;
				gm.achievementPoints = gmachievementPoints;
				gm.thumbnail = gmthumbnail;
			} else {
				gm = new Avatar();
				gm.name = gmname;
				gm.realm = gmrealm;
				gm.cclass = gmclass;
				gm.race = gmrace;
				gm.gender = gmgender;
				gm.level = gmlevel;
				gm.achievementPoints = gmachievementPoints;
				gm.thumbnail = gmthumbnail;

			}

			gm.avatar = fetchAvatar(gm.realm.name, gm.name, gm.thumbnail);
			gm.guild = g;
			gm.lastModified = new Date(gmlastModified);
			gm.isGuildMember = isGuildMember;
			gm.guildMember = guildm;
			gm.lastUpdate = new Date();
			gm.save();

			if (guildm != null) {
				guildm.wowcharacter = gm;
				guildm.hasWoWCharacter = true;
				guildm.achievementPoints = gmachievementPoints;
				guildm.avatar = fetchAvatar(gm.realm.name, gm.name, gm.thumbnail);
				guildm.save();
			}
			return gm;
		}
		return null;
	}

	public static void setRealms() {
		Realm realm = Realm.all().first();
		Boolean update = false;
		try {
			if (checkUpdate(new Date(), realm.lastUpdate, Armory.QUATERDAYUPDATE)) {
				update = true;
			}
		} catch (NullPointerException e) {
			update = true;
		}
		if (update) {
			Logger.info("Fetching Realms");
			fetchRealms();
		}
	}

	private static void fetchRealms() {
		JsonArray realms = fetchFromArmory(Armory.REALMSURL).getAsJsonObject().get("realms").getAsJsonArray();

		for (JsonElement jsonElement : realms) {
			String type = jsonElement.getAsJsonObject().get("type").getAsString();
			String population = jsonElement.getAsJsonObject().get("population").getAsString();
			Boolean queue = jsonElement.getAsJsonObject().get("queue").getAsBoolean();
			Boolean status = jsonElement.getAsJsonObject().get("status").getAsBoolean();
			String name = jsonElement.getAsJsonObject().get("name").getAsString();
			String slug = jsonElement.getAsJsonObject().get("slug").getAsString();
			String battlegroup = jsonElement.getAsJsonObject().get("battlegroup").getAsString();

			Realm realm = Realm.find("name = ?", name).first();

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

		}

	}

	public static void setMainGuild() {
		Guild mGuild = Guild.find("isMainGuild = ?", true).first();
		Boolean update = false;
		try {
			if (checkUpdate(new Date(), mGuild.lastUpdate, Armory.QUATERDAYUPDATE)) {
				update = true;
			}
		} catch (NullPointerException e) {
			update = true;
		}
		if (update) {
			Logger.info("Fetching Guild Infos");
			fetchGuild(true, null, null);
		}
	}

	public static Guild checkGuild(String name, Realm realm) {
		Guild mGuild = Guild.find("name = ?", name).first();

		Boolean update = false;
		try {
			if (checkUpdate(new Date(), mGuild.lastUpdate, Armory.QUATERDAYUPDATE)) {
				update = true;
			}
		} catch (NullPointerException e) {
			update = true;
		}
		if (update) {
			Logger.info("Fetching Guild Infos");
			return fetchGuild(false, name, realm);
		}
		return mGuild;
	}

	public static Guild fetchGuild(Boolean isMainGuild, String vname, Realm vrealm) {
		JsonObject mGuild;
		if (isMainGuild) {
			mGuild = getMainGuildJson();
		} else {
			mGuild = getGuildJson(vname, vrealm);
		}

		Long llastModified = mGuild.get("lastModified").getAsLong();
		String name = mGuild.get("name").getAsString();
		String srealm = mGuild.get("realm").getAsString();
		Long level = mGuild.get("level").getAsLong();
		Long lside = mGuild.get("side").getAsLong();
		Long achievementPoints = mGuild.get("achievementPoints").getAsLong();

		Date lastModified = new Date(llastModified);
		Realm realm = Realm.find("name = ? and region = ?", srealm, Play.configuration.getProperty("wowapi.realmArea")).first();

		if (realm == null) {
			realm = new Realm(srealm, Play.configuration.getProperty("wowapi.realmArea"));
		}

		Side side = Side.find("sId", lside).first();
		JsonObject emblem = mGuild.get("emblem").getAsJsonObject();

		GuildEmblem ge = GuildEmblem.find("guild = ?", name).first();
		if (ge != null) {
			ge.guild = name;
			ge.icon = emblem.get("icon").getAsLong();
			ge.iconColor = emblem.get("iconColor").getAsString();
			ge.border = emblem.get("border").getAsLong();
			ge.borderColor = emblem.get("borderColor").getAsString();
			ge.backgroundColor = emblem.get("backgroundColor").getAsString();
		} else {
			ge = new GuildEmblem();
			ge.guild = name;
			ge.icon = emblem.get("icon").getAsLong();
			ge.iconColor = emblem.get("iconColor").getAsString();
			ge.border = emblem.get("border").getAsLong();
			ge.borderColor = emblem.get("borderColor").getAsString();
			ge.backgroundColor = emblem.get("backgroundColor").getAsString();
			ge.save();
		}

		Guild g = Guild.find("name = ?", name).first();
		if (g != null) {
			g.name = name;
			g.realm = realm;
			g.level = level;
			g.side = side;
			g.achievementPoints = achievementPoints;
			g.lastModified = lastModified;
			g.lastUpdate = new Date();
			g.emblem = ge;
			g.isMainGuild = isMainGuild;
		} else {
			g = new Guild(lastModified, name, realm, level, side, achievementPoints, ge);
			g.isMainGuild = isMainGuild;
		}

		if (isMainGuild) {
			fetchGuildAchievements(mGuild);
			fetchGuildMembers(mGuild);
			g.members = (long) GuildMember.all().fetch().size();
		}

		JsonObject rankings = null;
		Long score = 0L;
		Long world_rank = 0L;
		Long area_rank = 0L;
		Long realm_rank = 0L;
		JsonElement eRankings = fetchFromWoWProgress(Armory.WOWPROGRESSURL + g.realm.region.toLowerCase() + "/" + replaceSpecialChars(g.realm.name.toLowerCase()) + "/" + encodeChars(g.name) + "/json_rank");
		if (eRankings.isJsonObject()) {
			rankings = eRankings.getAsJsonObject();
			score = rankings.get("score").getAsLong();
			world_rank = rankings.get("world_rank").getAsLong();
			area_rank = rankings.get("area_rank").getAsLong();
			realm_rank = rankings.get("realm_rank").getAsLong();
		}

		g.score = score;
		g.world_rank = world_rank;
		g.area_rank = area_rank;
		g.realm_rank = realm_rank;

		g.save();
		return g;
	}

	private static String encodeChars(String name) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
			name = name.replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

	private static String replaceSpecialChars(String realm) {
		// TODO Auto-generated method stub
		return realm.replace('\'', '-');
	}

	private static JsonElement fetchFromWoWProgress(String url) {
		Logger.info("Fetch URL: " + url);

		return WS.url(url).get().getJson();
	}

	private static JsonObject getGuildJson(String vname, Realm vrealm) {
		JsonObject mGuild = fetchFromArmory(Armory.GUILDURL, "achievements,members", vrealm.name, vname).getAsJsonObject();
		return mGuild;
	}

	public static JsonObject getMainGuildJson() {
		JsonObject mGuild = fetchFromArmory(Armory.GUILDURL, "achievements,members", Play.configuration.getProperty("wowapi.realmName"), Play.configuration.getProperty("wowapi.guildName")).getAsJsonObject();
		return mGuild;
	}

	public static void fetchGuildMembers(JsonObject mGuild) {
		JsonArray members = mGuild.get("members").getAsJsonArray();

		Logger.info("Fetching Guildmembers");
		
		for (JsonElement jsonElement : members) {

			Long rank = jsonElement.getAsJsonObject().get("rank").getAsLong();
			GuildRank gr = GuildRank.find("rank = ?", rank).first();
			if (gr == null) {
				gr = new GuildRank(rank);
				gr.save();
			}

			JsonObject character = jsonElement.getAsJsonObject().get("character").getAsJsonObject();

			String gmname = character.get("name").getAsString();
			String gmrealm = character.get("realm").getAsString();
			CharacterClass gmclass = CharacterClass.find("ccId", character.get("class").getAsLong()).first();
			CharacterRace gmrace = CharacterRace.find("crId", character.get("race").getAsLong()).first();
			Gender gmgender = Gender.find("gId", character.get("gender").getAsLong()).first();
			// Long gmgender = avatar.get("gender").getAsLong();
			Long gmlevel = character.get("level").getAsLong();
			GuildRank gmrank = GuildRank.find("rank", gr.rank).first();
			Long gmachievementPoints = character.get("achievementPoints").getAsLong();
			String gmthumbnail = character.get("thumbnail").getAsString();

			GuildMember gm = GuildMember.find("name = ?", gmname).first();

			if (gm != null) {
				gm.name = gmname;
				gm.realm = gmrealm;
				gm.cclass = gmclass;
				gm.race = gmrace;
				gm.gender = gmgender;
				gm.level = gmlevel;
				gm.rank = gmrank;
				gm.achievementPoints = gmachievementPoints;
				gm.thumbnail = gmthumbnail;
				gm.lastUpdate = new Date();
				gm.save();
				gm.avatar = fetchAvatar(gm.realm, gm.name, gm.thumbnail);
				gm.wowcharacter = fetchCharacter(gmrealm, gmname);
				gm.save();
			} else {
				gm = new GuildMember(gmname, gmrealm, gmclass, gmrace, gmgender, gmlevel, gmrank, gmachievementPoints, gmthumbnail);
				gm.save();
				gm.avatar = fetchAvatar(gm.realm, gm.name, gm.thumbnail);
				gm.wowcharacter = fetchCharacter(gmrealm, gmname);
				gm.save();
			}
		}
	}

	public static void fetchGuildAchievements(JsonObject mGuild) {
		JsonObject achievements = mGuild.get("achievements").getAsJsonObject();
		JsonArray achievementsCompleted = achievements.get("achievementsCompleted").getAsJsonArray();
		JsonArray achievementsCompletedTimestamp = achievements.get("achievementsCompletedTimestamp").getAsJsonArray();
		JsonArray criteria = achievements.get("criteria").getAsJsonArray();
		JsonArray criteriaQuantity = achievements.get("criteriaQuantity").getAsJsonArray();
		JsonArray criteriaTimestamp = achievements.get("criteriaTimestamp").getAsJsonArray();
		JsonArray criteriaCreated = achievements.get("criteriaCreated").getAsJsonArray();

		int count = 0;
		for (JsonElement fachievementsCompleted : achievementsCompleted) {
			Long laId = achievementsCompleted.get(count).getAsLong();
			Date dtimestamp = new Date(achievementsCompletedTimestamp.get(count).getAsLong());
			Long lcriteria = criteria.get(count).getAsLong();
			Long lcriteriaQuantity = criteriaQuantity.get(count).getAsLong();
			Date dcriteriaTimestamp = new Date(criteriaTimestamp.get(count).getAsLong());
			Date dcriteriaCreated = new Date(criteriaCreated.get(count).getAsLong());

			GuildAchievement ga = GuildAchievement.find("aId = ?", laId).first();
			if (ga != null) {
				ga.aId = laId;
				ga.timestamp = dtimestamp;
				ga.criteria = lcriteria;
				ga.criteriaQuantity = lcriteriaQuantity;
				ga.criteriaTimestamp = dcriteriaTimestamp;
				ga.criteriaCreated = dcriteriaCreated;
				ga.save();
			} else {
				ga = new GuildAchievement(laId, dtimestamp, lcriteria, lcriteriaQuantity, dcriteriaTimestamp, dcriteriaCreated);
				ga.save();
			}
			count++;
		}
	}

	public static void setItemClasses() {
		ItemClass iClass = ItemClass.all().first();
		Boolean update = false;
		try {
			if (checkUpdate(new Date(), iClass.lastUpdate, Armory.WEEKLYUPDATE)) {
				update = true;
			}
		} catch (NullPointerException e) {
			update = true;
		}
		if (update) {
			Logger.info("Fetching Items Classes");
			fetchItemClasses();
		}
	}

	private static void fetchItemClasses() {
		JsonArray classes = fetchFromArmory(Armory.ITEMCLASSESURL).getAsJsonObject().get("classes").getAsJsonArray();

		for (JsonElement jsonElement : classes) {
			Long classId = jsonElement.getAsJsonObject().get("class").getAsLong();
			String name = jsonElement.getAsJsonObject().get("name").getAsString();

			ItemClass ic = ItemClass.find("byClassId", classId).first();
			if (ic == null) {
				ic = new ItemClass(classId, name);
				ic.save();
			} else {
				ic.classId = classId;
				ic.name = name;
				ic.lastUpdate = new Date();
				ic.save();
			}
		}

	}

	public static void setGuildPerks() {
		GuildPerk gPerk = GuildPerk.all().first();
		Boolean update = false;
		try {
			if (checkUpdate(new Date(), gPerk.lastUpdate, Armory.WEEKLYUPDATE)) {
				update = true;
			}
		} catch (NullPointerException e) {
			update = true;
		}
		if (update) {
			Logger.info("Fetching Guild Perks");
			fetchGuildPerks();
		}
	}

	private static void fetchGuildPerks() {
		JsonArray perks = fetchFromArmory(Armory.GUILDPERKSURL).getAsJsonObject().get("perks").getAsJsonArray();

		for (JsonElement jsonElement : perks) {
			Long guildLevel = jsonElement.getAsJsonObject().get("guildLevel").getAsLong();
			JsonObject spells = jsonElement.getAsJsonObject().get("spell").getAsJsonObject();

			Long gpsId = spells.get("id").getAsLong();
			String name = spells.get("name").getAsString();
			String subtext = "";

			try {
				subtext = spells.get("subtext").getAsString();
			} catch (NullPointerException e) {
				// TODO: handle exception
			}

			String icon = spells.get("icon").getAsString();
			String description = spells.get("description").getAsString();

			GuildPerk gPerk = GuildPerk.find("byGuildLevel", guildLevel).first();
			if (gPerk == null) {
				gPerk = new GuildPerk(guildLevel);
				gPerk.save();
			} else {
				gPerk.guildLevel = guildLevel;
				gPerk.lastUpdate = new Date();
				gPerk.save();

			}

			GuildPerkSpell gpSpell = GuildPerkSpell.find("gpsId = ?", gpsId).first();
			if (gpSpell == null) {
				gpSpell = new GuildPerkSpell(gPerk, gpsId, name, subtext, icon, description);
				gpSpell.save();
			} else {
				gpSpell.guildperk = gPerk;
				gpSpell.gpsId = gpsId;
				gpSpell.name = name;
				gpSpell.subtext = subtext;
				gpSpell.icon = icon;
				gpSpell.description = description;
				gpSpell.lastUpdate = new Date();
			}

		}
	}

	public static void setCharacterRaces() {
		CharacterRace cRace = CharacterRace.all().first();
		Boolean update = false;
		try {
			if (checkUpdate(new Date(), cRace.lastUpdate, Armory.WEEKLYUPDATE)) {
				update = true;
			}
		} catch (NullPointerException e) {
			update = true;
		}
		if (update) {
			Logger.info("Fetching Character Races");
			fetchCharacterRaces();
		}
	}

	private static void fetchCharacterRaces() {
		JsonArray races = fetchFromArmory(Armory.CHARACTERRACESURL).getAsJsonObject().get("races").getAsJsonArray();

		for (JsonElement jsonElement : races) {
			Long crId = jsonElement.getAsJsonObject().get("id").getAsLong();
			Long mask = jsonElement.getAsJsonObject().get("mask").getAsLong();
			String name = jsonElement.getAsJsonObject().get("name").getAsString();
			String sside = jsonElement.getAsJsonObject().get("side").getAsString();
			Side side = Side.find("name = ?", sside).first();
			CharacterRace cr = CharacterRace.find("byCrId", crId).first();
			if (cr == null) {
				cr = new CharacterRace(crId, mask, side, name);
				cr.save();
			} else {
				cr.mask = mask;
				cr.name = name;
				cr.side = side;
				cr.lastUpdate = new Date();
				cr.save();
			}

		}

	}

	public static void setCharacterClasses() {
		CharacterClass cClass = CharacterClass.all().first();
		Boolean update = false;
		try {
			if (checkUpdate(new Date(), cClass.lastUpdate, Armory.WEEKLYUPDATE)) {
				update = true;
			}
		} catch (NullPointerException e) {
			update = true;
		}
		if (update) {
			Logger.info("Fetching Character Classes");
			fetchCharacterClasses();
		}
	}

	private static void fetchCharacterClasses() {
		JsonArray classes = fetchFromArmory(Armory.CHARACTERCLASSESURL).getAsJsonObject().get("classes").getAsJsonArray();

		for (JsonElement jsonElement : classes) {
			Long ccId = jsonElement.getAsJsonObject().get("id").getAsLong();
			Long mask = jsonElement.getAsJsonObject().get("mask").getAsLong();
			String name = jsonElement.getAsJsonObject().get("name").getAsString();
			String powerType = jsonElement.getAsJsonObject().get("powerType").getAsString();

			CharacterClass cc = CharacterClass.find("byCcId", ccId).first();
			if (cc == null) {
				cc = new CharacterClass(ccId, mask, name, powerType);
				cc.save();
			} else {
				cc.mask = mask;
				cc.name = name;
				cc.powerType = powerType;
				cc.lastUpdate = new Date();
				cc.save();
			}

		}

	}

	private static JsonElement fetchFromArmory(String moduleURL, String fields, String realm, String guild, String character, String locale, String region) {
		Realm guildRealm = Realm.getGuilRealm();

		if (region == null) {
			region = guildRealm.region.toLowerCase();
		}
		if (locale == null) {
			locale = Play.configuration.getProperty("wowapi.locale");
		}

		if (realm != null) {
			try {
				realm = URLEncoder.encode(realm, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (guild != null) {
			try {
				guild = URLEncoder.encode(guild, "UTF-8");
				guild = guild.replaceAll("\\+", "%20");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (character != null) {
			try {
				character = URLEncoder.encode(character, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String battleNetURL = "http://" + region + ".battle.net";
		// String guildAvatarURL = "http://" + region +
		// ".battle.net/static-render/" + region + "/";

		String battleNetApiURL = "";

		if (guild == null && character == null) {
			battleNetApiURL = battleNetURL + moduleURL + "?locale=" + locale;
		}

		if (guild != null && character == null) {
			battleNetApiURL = battleNetURL + moduleURL + realm + "/" + guild + "?locale=" + locale;
		}

		if (guild == null && character != null) {
			battleNetApiURL = battleNetURL + moduleURL + realm + "/" + character + "?locale=" + locale;
		}

		if (fields != null) {
			battleNetApiURL = battleNetApiURL + "&fields=" + fields;
		}

		Logger.info("Fetch URL: " + battleNetApiURL);

		HttpResponse hr = WS.url(battleNetApiURL).get();
		if (hr.success()) {
			
			System.out.println(hr.getStatus() );
			
			JsonElement armoryanswer = hr.getJson();
			return armoryanswer;
		} else {
			Logger.error("Fetch failed: (" + hr.getStatus() + ") " + battleNetApiURL);
			return null;
		}

	}

	private static JsonElement fetchFromArmory(String moduleURL, String fields, String realm, String guild, String character, String locale) {
		return fetchFromArmory(moduleURL, fields, realm, guild, character, locale, null);
	}

	private static JsonElement fetchFromArmory(String moduleURL, String fields, String realm, String guild, String character) {
		return fetchFromArmory(moduleURL, fields, realm, guild, character, null);
	}

	private static JsonElement fetchFromArmory(String moduleURL, String fields, String realm, String guild) {
		return fetchFromArmory(moduleURL, fields, realm, guild, null);
	}

	private static JsonElement fetchFromArmory(String moduleURL, String fields, String realm) {
		return fetchFromArmory(moduleURL, fields, realm, null);
	}

	private static JsonElement fetchFromArmory(String moduleURL, String fields) {
		return fetchFromArmory(moduleURL, fields, null);
	}

	private static JsonElement fetchFromArmory(String moduleURL) {
		return fetchFromArmory(moduleURL, null);
	}

	public static boolean checkUpdate(Date currDate, Date lastDate, long intervall) {
		if ((currDate.getTime() - intervall) > lastDate.getTime()) {
			return true;
		}
		return false;
	}

	public static String fetchChar(String name, Realm realm) {

		System.out.println(name + realm);

		String realmurl = "http://" + realm.region.toLowerCase() + ".battle.net";
		String avatarurl = "http://" + realm.region.toLowerCase() + ".battle.net/static-render/" + realm.region.toLowerCase() + "/";

		String ApiURL = realmurl + "/api/wow/character/" + realm.name + "/" + name + "?locale=de_DE&fields=guild,stats,items,reputation,titles,professions,appearance,achievements,progression,pvp";
		System.out.println(ApiURL);

		JsonElement armorychar = WS.url(ApiURL).get().getJson();

		System.out.println(armorychar.isJsonObject());

		if (armorychar.isJsonObject()) {
			JsonObject armoryInfos = armorychar.getAsJsonObject();
			JsonObject guild;
			if (!armoryInfos.get("name").isJsonNull()) {
				System.out.println(armoryInfos.get("name").getAsString());
				System.out.println(avatarurl + armoryInfos.get("thumbnail").getAsString());
				System.out.println(guild = armoryInfos.get("guild").getAsJsonObject());
				// System.out.println(guild.get("emblem").getAsJsonObject());
				// System.out.println(armoryInfos.get("items").getAsJsonObject());

				// String jsguildemblem =
				// guild.get("emblem").getAsJsonObject().toString();
				String jsguildemblem = armoryInfos.toString();
				HashMap<String, String> guildemblem = new HashMap<String, String>();
				guildemblem = new JSONDeserializer<HashMap<String, String>>().deserialize(jsguildemblem);

				System.out.println(guildemblem.get("backgroundColor"));

			}

		}

		return name;

	}

}
