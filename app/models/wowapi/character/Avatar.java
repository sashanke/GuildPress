package models.wowapi.character;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.wowapi.Armory;
import models.wowapi.guild.Guild;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.Gender;
import models.wowapi.resources.Item;
import models.wowapi.resources.ItemSlotType;
import models.wowapi.resources.RaceClassMap;
import models.wowapi.resources.Realm;
import models.wowapi.resources.RecipeReagent;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Codec;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Router;
import utils.Tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Entity
public class Avatar extends Model {
	
	@Override
	public String toString() {
		return this.getAvatarMail();
	}
	
	public static void createAsyncAvatar(String name, String realm) {
		Promise<Avatar> futureAvatar = new Promise<Avatar>();
		futureAvatar.invoke(Avatar.createAvatar(name, realm));
	}

	/**
	 * Creates an Avatar
	 * 
	 * @param avatar
	 * 
	 * @return Avatar
	 */
	private static Avatar createAvatar(Avatar avatar) {
		Logger.info("[Avatar][createAvatar] " + avatar.name + " (" + avatar.realm + ")");
		return fetchAvatar(avatar);
	}

	/**
	 * Creates an Avatar
	 * 
	 * @param name
	 * @param realm
	 * @return Avatar
	 */
	public static Avatar createAvatar(String name, String realm) {
		Avatar avatar = Avatar.findByNameAndRealm(name, realm);
		if (avatar == null) {
			return createAvatar(new Avatar(name, realm));
		} else {
			return updateAvatar(avatar);
		}
	}

	public static Avatar fetchAvatar(Avatar avatar) {
		Logger.info("[Avatar][fetchAvatar] " + avatar.name + " (" + avatar.realm + ")");
		JsonObject avatarJson = Armory.getAvatarJson(avatar.name, avatar.realm);
		if (avatarJson == null) {
			Avatar m = Avatar.findByNameAndRealm(avatar.name, avatar.realm);
			if (m != null) {
				Logger.warn("[Avatar][fetchAvatar] Armory Error/Down but Char was found in Database: " + m.name + " (" + m.realm + ")");
				return m;
			}
			GuildMember gm = GuildMember.findByNameAndRealm(avatar.name, avatar.realm);
			if (gm != null && m == null) {
				Logger.warn("[Avatar][fetchAvatar] Creating new Char from Guildmember: " + gm.name + " (" + gm.realm + ")");

				avatar.achievementPoints = 0L;
				avatar.image = gm.image;
				avatar.guild = gm.guild;
				avatar.image = gm.image;
				avatar.cclass = gm.cclass;
				avatar.gender = gm.gender;
				avatar.lastUpdate = new Date();
				avatar.level = gm.level;
				avatar.name = gm.name;
				avatar.race = gm.race;
				avatar.realm = gm.realm;
				avatar.isGuildMember = true;
				avatar.guildMember = gm;
				avatar.lastModified = gm.lastModified;
				avatar.save();
				gm.avatar = avatar;
				gm.save();
				return avatar;
			}
		}

		if (avatarJson == null) {
			avatar.cclass = CharacterClass.all().first();
			avatar.race = CharacterRace.all().first();
			avatar.gender = Gender.all().first();
			avatar.level = 0L;
			avatar.achievementPoints = 0L;
			avatar.lastModified = new Date(0L);
			avatar.isGuildMember = false;
			avatar.lastUpdate = new Date();
			avatar.save();
			return avatar;
		}
		
		if (avatarJson.has("guild")) {
			String avatarRealm = avatarJson.getAsJsonObject("guild").get("realm").getAsString();
			String avatarGuildName = avatarJson.getAsJsonObject("guild").get("name").getAsString();
			Long avatarGuildMembers = avatarJson.getAsJsonObject("guild").get("members").getAsLong();
			Guild avatarGuild = Guild.createGuild(avatarGuildName, avatarRealm);
			avatarGuild.members = avatarGuildMembers;
			avatarGuild.save();
			avatar.guild = avatarGuild;
		}

		String gmname = avatarJson.get("name").getAsString();
		Realm gmrealm = Realm.find("name", avatarJson.get("realm").getAsString()).first();
		CharacterClass gmclass = CharacterClass.find("ccId", avatarJson.get("class").getAsLong()).first();
		CharacterRace gmrace = CharacterRace.find("crId", avatarJson.get("race").getAsLong()).first();
		Gender gmgender = Gender.find("gId", avatarJson.get("gender").getAsLong()).first();
		Long gmlevel = avatarJson.get("level").getAsLong();
		Long gmachievementPoints = avatarJson.get("achievementPoints").getAsLong();
		String gmthumbnail = avatarJson.get("thumbnail").getAsString();
		Long gmlastModified = avatarJson.get("lastModified").getAsLong();

		JsonObject items = avatarJson.get("items").getAsJsonObject();

		JsonObject professions = avatarJson.get("professions").getAsJsonObject();

		Long averageItemLevel = items.get("averageItemLevel").getAsLong();
		Long averageItemLevelEquipped = items.get("averageItemLevelEquipped").getAsLong();

		GuildMember guildMemeber = GuildMember.findByNameAndRealm(gmname, gmrealm.name);
		Boolean isGuildMember = false;

		if (guildMemeber != null) {
			isGuildMember = true;
		}

		avatar.name = gmname;
		avatar.realm = gmrealm;
		avatar.cclass = gmclass;
		avatar.race = gmrace;
		avatar.gender = gmgender;
		avatar.level = gmlevel;
		avatar.achievementPoints = gmachievementPoints;
		avatar.image = Armory.fetchAvatar(avatar.realm, avatar.name, gmthumbnail);
		avatar.profile = Armory.fetchProfile(avatar.realm, avatar.name, gmthumbnail.replace("avatar", "profilemain"));
		avatar.inset = Armory.fetchInset(avatar.realm, avatar.name, gmthumbnail.replace("avatar", "inset"));
		avatar.lastModified = new Date(gmlastModified);
		avatar.isGuildMember = isGuildMember;
		avatar.guildMember = guildMemeber;
		avatar.lastUpdate = new Date();

		avatar.averageItemLevel = averageItemLevel;
		avatar.averageItemLevelEquipped = averageItemLevelEquipped;
		avatar.save();

		if (avatar.isGuildMember) {
			List<AvatarProfession> professionsList = AvatarProfession.createProfessions(professions, avatar);
			avatar.professions = professionsList;
			avatar.save();
		}

		if (guildMemeber != null) {
			guildMemeber.image = avatar.getImage();
			guildMemeber.avatar = avatar;
			guildMemeber.save();
		}

		HashMap<String, JsonObject> itemMap = new HashMap<String, JsonObject>();

		Armory.getItemsFromJson(items, itemMap);

		AvatarItem.delete("avatar = ?", avatar);

		for (JsonObject item : itemMap.values()) {

			AvatarItem at = new AvatarItem();

			Long itemId = item.get("id").getAsLong();

			JsonObject tooltipParams = item.get("tooltipParams").getAsJsonObject();
			Long transmogItemId = 0L;
			Item itemO = Item.setItem(itemId);
			Item transmogItem = null;
			String tooltipParamsS = "";
			if (tooltipParams.has("transmogItem")) {
				transmogItemId = tooltipParams.get("transmogItem").getAsLong();
				transmogItem = Item.setItem(transmogItemId);
			}
			tooltipParamsS = tooltipParams.toString();

			String slottype = item.get("slottype").getAsString();
			at.itemId = itemId;
			at.transmogItemId = transmogItemId;
			at.tooltipParams = tooltipParamsS;
			at.avatar = avatar;
			at.item = itemO;
			at.itemSlot = ItemSlotType.setItemSlotType(slottype, itemO.slot);
			at.transmogItem = transmogItem;

			JsonParser parser = new JsonParser();
			if (tooltipParamsS.length() != 0) {
				JsonObject o = (JsonObject) parser.parse(tooltipParamsS);
				List<String> urlParts = new ArrayList<String>();
				String enchant = "enchant";
				if (o.has(enchant)) {
					urlParts.add("e=" + o.get(enchant).getAsString());
				}
				String gem0 = "gem0";
				if (o.has(gem0)) {
					urlParts.add("g0=" + o.get(gem0).getAsString());
				}
				String gem1 = "gem1";
				if (o.has(gem1)) {
					urlParts.add("g1=" + o.get(gem1).getAsString());
				}
				String gem2 = "gem2";
				if (o.has(gem2)) {
					urlParts.add("g2=" + o.get(gem2).getAsString());
				}
				String gem3 = "gem3";
				if (o.has(gem3)) {
					urlParts.add("g3=" + o.get(gem3).getAsString());
				}
				String gem4 = "gem4";
				if (o.has(gem4)) {
					urlParts.add("g4=" + o.get(gem4).getAsString());
				}
				String reforge = "reforge";
				if (o.has(reforge)) {
					urlParts.add("re=" + o.get(reforge).getAsString());
				}
				String transmogItemid = "transmogItem";
				if (o.has(transmogItemid)) {
					urlParts.add("t=" + o.get(transmogItemid).getAsString());
				}
				String set = "set";
				if (o.has(set)) {
					JsonArray sets = o.get(set).getAsJsonArray();
					urlParts.add("set=" + Tools.implodeArray(sets, ","));
				}
				String url = "http://eu.battle.net/wow/de/item/" + itemId + "/tooltip?" + Tools.implodeList(urlParts, "&");
				at.armoryTooltipURL = url;
				HttpResponse hr = WS.url(url).get();

				if (hr.success()) {
					at.armoryTooltip = hr.getString();
				}

			}
			avatar.addItem(at);
		}
		avatar.save();

		return avatar;
	}

	/**
	 * Fetch an specific Avatar from the WoW Armory
	 * 
	 * @param name
	 * @param realm
	 * @return Avatar
	 */
	public static Avatar fetchAvatar(String name, String realm) {
		return fetchAvatar(Avatar.findByNameAndRealm(name, realm));
	}

	/**
	 * Finds an avatar by name and realm, Binary Safe!
	 * 
	 * @param name
	 * @param realm
	 * @return Avatar
	 */
	public static Avatar findByNameAndRealm(String name, Realm realm) {
		return Avatar.find("hash = ?", Codec.hexMD5(name)).first();
		// try {
		// PreparedStatement ps =
		// DB.getConnection().prepareStatement("select id from Avatar where BINARY name = ? and realm_id = ?");
		// ps.setString(1, name);
		// ps.setLong(2, realm.id);
		// ResultSet rs = ps.executeQuery();
		// if (rs.first()) {
		// Logger.info("[Avatar][findByNameAndRealm] Avatar " + name +
		// " found");
		// return Avatar.findById(rs.getLong("id"));
		// } else {
		// Logger.info("[Avatar][findByNameAndRealm] Avatar " + name +
		// " not found");
		// return null;
		// }
		//
		// } catch (SQLException e) {
		// Logger.warn("[Avatar][FindByNameAndRealm] " +
		// e.getLocalizedMessage(), e);
		// return null;
		// }

	}

	/**
	 * Finds an avatar by name and realm, Binary Safe!
	 * 
	 * @param name
	 * @param realm
	 * @return Avatar
	 */
	public static Avatar findByNameAndRealm(String name, String realm) {
		return findByNameAndRealm(name, Realm.findByName(realm));
	}

	/**
	 * Update an Avatar
	 * 
	 * @param avatar
	 * 
	 * @return Avatar
	 */
	public static Avatar updateAvatar(Avatar avatar) {
		if (Armory.checkUpdate(avatar.lastUpdate, Armory.QUATERDAYUPDATE)) {
			Logger.info("[Avatar][updateAvatar] " + avatar.name + " (" + avatar.realm + ")");
			return fetchAvatar(avatar);
		}
		return avatar;
	}
	
	/**
	 * Update an Avatar
	 * 
	 * @param avatar
	 * 
	 * @return Avatar
	 */
	public static Avatar updateAvatar(Avatar avatar, Boolean force) {
		Logger.info("[Avatar][updateAvatar] " + avatar.name + " (" + avatar.realm + ")");
		return fetchAvatar(avatar);
	}
	

	@Required
	public String name;

	public String hash;

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

	public String image;

	public String profile;

	public String inset;

	@ManyToOne
	public Guild guild;

	@OneToOne
	public GuildMember guildMember;

	public Boolean isGuildMember;

	public Date lastModified;

	public Date lastUpdate;

	public Date created;

	@SuppressWarnings("unused")
	private String avatarLink;

	@OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL)
	public List<AvatarItem> items;

	@OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL)
	public List<AvatarProfession> professions;

	public Long averageItemLevel;

	public Long averageItemLevelEquipped;

	public Avatar(String name, String realm) {
		this.name = name;
		this.hash = Codec.hexMD5(name);
		this.realm = Realm.findByName(realm);
		this.lastModified = new Date();
		this.lastUpdate = new Date();
		this.created = new Date();
		this.items = new ArrayList<AvatarItem>();
		this.averageItemLevel = 0L;
		this.averageItemLevelEquipped = 0L;
	}

	public Avatar addItem(AvatarItem item) {
		item.save();
		this.items.add(item);
		this.save();
		return this;
	}

	public String getAvatarBanner() {
		String banner = Play.configuration.getProperty("conf.bannerdir") + this.race.side.name.toLowerCase() + "/" + this.race.name.toLowerCase() + "/" + this.race.name.toLowerCase() + "_" + Tools.replaceUmlauts(this.cclass.name.toLowerCase()) + "_"
				+ Tools.replaceUmlauts(this.gender.name_loc.toLowerCase()) + ".jpg";
		return banner;
	}

	public String getAvatarMail() {
		String mail = this.name.toLowerCase();
		mail += "@" + this.realm.name.toLowerCase();
		mail += "." + this.realm.region.toLowerCase();
		return mail;
	}
	
	/**
	 * For javascript/json!
	 */
	public String getAvatarLink() {
		Map<String, Object> args = new HashMap<String, Object>();

		args.put("id", this.id);
		args.put("name", this.name);
		args.put("realm", this.realm.name);

		return "<a href=\"" + Router.getFullUrl("Char.show", args) + "\" class=\"class class-" + this.cclass.name.toLowerCase() + "\">" + this.name + "</a>";
	}

	/**
	 * @return the avatar
	 */
	public String getImage() {
		if (this.image == null || this.image.contains("noavatar.png")) {
			return "/public/images/static/avatar/" + this.race.crId + "-" + this.gender.gId + ".jpg";
		}
		if (this.image.startsWith(".")) {
			return this.image.substring(1);
		}
		return this.image;
	}

	/**
	 * @return the inset
	 */
	public String getInset() {
		if (this.inset == null) {
			return "/public/images/static/inset/" + this.race.crId + "-" + this.gender.gId + ".jpg";
		}
		if (this.inset.startsWith(".")) {
			return this.inset.substring(1);
		}
		return this.inset;
	}

	public String getKeywords() {
		String keywords = this.name;
		if (this.guild != null) {
			keywords += ", " + this.guild.name;
		}
		keywords += ", " + this.race.name;
		keywords += ", " + this.cclass.name;
		keywords += ", " + this.level;
		keywords += ", " + this.realm.name;
		keywords += ", " + this.gender.name;
		keywords += ", " + this.race.side.name;
		return keywords;
	}
	
	public String getDescription() {
		String description = "Informationen über " + this.name + " ein " + this.race.name + " " + this.cclass.name + " mit Level " + this.level + " auf dem Server " + this.realm.name;
		if (this.guild != null) {
			description += " und Mitglied der Gilde " + this.guild.name;
		}
		description += ".";
		if (this.professions != null) {
			description += " " + this.name + " hat die Berufe: ";
			for (AvatarProfession profession : this.professions) {
				description += profession.name + " (" + profession.rank + "), ";
			}
			description = description.substring(0, description.length() - 2);
		}
		description += ".";

		description += " " + this.name + " trägt folgende Gegenstände: ";
		
		List<AvatarItem> items =  AvatarItem.find("byAvatar", this).fetch();
		
		for (AvatarItem item : items ) {
			description += item.item.name + ", ";
		}
		description = description.substring(0, description.length() - 2);
		return description;
	}
	
	
	/**
	 * @return the profile
	 */
	public String getProfile() {
		if (this.profile == null) {
			return "/public/images/static/profilemain/" + this.race.crId + "-" + this.gender.gId + ".jpg";
		}
		if (this.profile.startsWith(".")) {
			return this.profile.substring(1);
		}
		return this.profile;
	}

	public String getProfileMain() {
		if (this.profile == null) {
			return "/public/images/static/profilemain/" + this.race.crId + "-" + this.gender.gId + ".jpg";
		}
		return this.profile;
	}
}
