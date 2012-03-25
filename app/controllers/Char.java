package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import models.raidtracker.RaidItem;
import models.wowapi.Armory;
import models.wowapi.WoWHead;
import models.wowapi.character.Avatar;
import models.wowapi.character.AvatarItem;
import models.wowapi.guild.Guild;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.Item;
import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Tools;

public class Char extends Controller {
	
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
	
	public static void show(Long id, String name, String realm) {	
		Avatar avatar = Avatar.findById(id);
		List<AvatarItem> items = AvatarItem.getOrderedItemList(avatar);
		render(avatar,items);
	}
	public static void showItem(Long id) {
		Item item = Item.find("byItemId",id).first();
		if (item == null) {
			item = WoWHead.checkItem(id);
		}
		List<RaidItem> items = RaidItem.find("itemId = ?", item.itemId).fetch();
		List<AvatarItem> wearedItems = AvatarItem.find("itemId = ?", item.itemId).fetch();
		render(item, items, wearedItems);
	}
	public static void showItemTooltip(Long id) {
		Item item = Item.find("byItemId",id).first();
		if (item == null) {
			item = WoWHead.checkItem(id);
		}
		render(item);
	}
	
	public static void showArmoryItemTooltip(Long avatarItemId) {
		AvatarItem item = AvatarItem.findById(avatarItemId);
		render(item);
	}
	
}
