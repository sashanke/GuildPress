package controllers;

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
import models.wowapi.resources.Item;
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
	
	public static void show(String name) {
		Armory.fetchCharacter("Anub'arak", name);
		Armory.fetchCharacter(true,"Anub'arak", name);
		Avatar avatar = Avatar.find("name = ?", name).first();
		List<AvatarItem> items = AvatarItem.getOrderedItemList(avatar);
		
		render(avatar,items);
	}
	public static void showItem(Long id) {
		Item item = Item.find("byItemId",id).first();
		if (item == null) {
			item = WoWHead.checkItem(id);
		}
		render(item);
	}
	
	public static void showArmoryItem(Long avatarItemId) {
		AvatarItem item = AvatarItem.findById(avatarItemId);
		render(item);
	}
	
}
