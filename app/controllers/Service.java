package controllers;

import java.util.Date;
import java.util.List;

import flexjson.JSONSerializer;
import models.wowapi.Armory;
import models.wowapi.ArmoryTooltip;
import models.wowapi.Type;
import models.wowapi.character.Avatar;
import models.wowapi.guild.Guild;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.Gender;
import models.wowapi.resources.Item;
import models.wowapi.resources.Realm;
import models.wowapi.resources.Recipe;
import models.wowapi.resources.Side;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

public class Service extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
	
	public static void updateGuild(String name, String realm) {
		Logger.info("[Service][Request][updateGuild] " + name + " (" + realm + ")");
		JSONSerializer guildSerializer = new JSONSerializer().prettyPrint(true);
		renderJSON(guildSerializer.serialize(Guild.createGuild(name, realm)));
	}

	public static void updateRealms() {
		Logger.info("[Service][Request][updateRealms] all Realms");
		Realm.fetchRealms();
		JSONSerializer realmSerializer = new JSONSerializer().prettyPrint(true);
		renderJSON(realmSerializer.serialize(Realm.findAll()));
	}

	public static void updateAvatar(String name, String realm) {

		List<Recipe> recipes = Recipe.find("spellId = ?", 101937L).fetch(1);
		for (Recipe recipe : recipes) {
			recipe.setTooltip();
			// System.out.println(recipe.armoryTooltip);
		}
		// renderHtml("");

		Logger.info("[Service][Request][updateAvatar] " + name + " (" + realm + ")");
		JSONSerializer avatarSerializer = new JSONSerializer().prettyPrint(true);
		renderJSON(avatarSerializer.serialize(Avatar.createAvatar(name, realm)));
	}

	public static void updateItem(Long itemId) {
		Logger.info("[Service][Request][updateItem] " + itemId);
		JSONSerializer itemSerializer = new JSONSerializer().prettyPrint(true);
		renderJSON(itemSerializer.serialize(Item.setItem(itemId)));
	}
	
	public static void updateRecipe(Long spellId) {
		Logger.info("[Service][Request][updateRecipe] " + spellId);
		
		Recipe recipe = Recipe.find("spellId = ?", spellId).first();		
		recipe.setTooltip();
		
		JSONSerializer spellSerializer = new JSONSerializer().prettyPrint(true);
		renderJSON(spellSerializer.serialize(recipe));
	}


	public static void showTooltip(Long id, String type) {
		ArmoryTooltip toolTip = ArmoryTooltip.findById(id);
		render("/Service/show" + type + "Tooltip.html",toolTip);
	}
	
	public static void showRecipe(Long id) {
		Recipe recipe = Recipe.findById(id);
		render(recipe);
	}
	public static void update() {
		List<Recipe> recipes = Recipe.find("item_id is null and name is not null").fetch();
		for (Recipe recipe : recipes) {
			recipe.setTooltip();
		}
	}
	
}
