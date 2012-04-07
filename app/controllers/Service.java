package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import models.wowapi.ArmoryTooltip;
import models.wowapi.AuctionData;
import models.wowapi.character.Avatar;
import models.wowapi.guild.Guild;
import models.wowapi.resources.Item;
import models.wowapi.resources.Realm;
import models.wowapi.resources.Recipe;
import models.wowapi.resources.helper.RecipeShoppingCart;
import play.Logger;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import flexjson.JSONSerializer;

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
		if (type.equals("Spell")) {
			ArmoryTooltip toolTip = ArmoryTooltip.findById(id);
			render("/Service/show" + type + "Tooltip.html", toolTip);
		}
		if (type.equals("RecipeOptions")) {
			Recipe recipe = Recipe.findById(id);
			render("/Service/show" + type + "Tooltip.html", recipe);
		}
	}

	public static void upLoadItemPrices() {
		render();
	}

	public static void updateItemPrices(File lua) throws FileNotFoundException {
		AuctionData.parseFile(lua);
		
		// StringBuilder sb = new StringBuilder();
		// try {
		// BufferedReader in = new BufferedReader(new FileReader(lua));
		// String str;
		// while ((str = in.readLine()) != null) {
		// sb.append(str + "\n");
		// }
		// in.close();
		// } catch (IOException e) {
		// }
		//
		// String luaString = sb.toString();
		// String dataString = "";
		// Date updated = new Date();
		// Pattern pattern =
		// Pattern.compile("(?ism)(.*?)(\\[\"ropes\"\\] = \\{.*?\"return \\{)(.*?)(\\},\\}\", --.*)");
		// Matcher matcher = pattern.matcher(luaString);
		// if (matcher.find()) {
		// luaString = matcher.group(3);
		// dataString = matcher.group(1) + matcher.group(2) + matcher.group(4);
		// }
		//
		// pattern =
		// Pattern.compile("(?ism)(\\[\"ImageUpdated\"\\] = )([0-9]{9,12})");
		// matcher = pattern.matcher(dataString);
		// if (matcher.find()) {
		// updated.setTime(Long.parseLong(matcher.group(2)) * 1000);
		// }
		//
		// AuctionData ad = AuctionData.find("updated = ?", updated).first();
		// if (ad == null) {
		// String[] itemLines = luaString.split("\\},\\{");
		//
		// for (String string : itemLines) {
		// String auctionLine = string.replaceAll("\\\\\"", "");
		// auctionLine = auctionLine.substring(0, auctionLine.length() - 1);
		// String[] itemData = auctionLine.split(",");
		// // |cffa335ee|Hitem:71980:0:0:0:0:0:0:319052448:80:0|h[Beinwickel
		// // des
		// //
		// Lavabebens]|h|r\",397,\"Rüstung\",\"Stoff\",7,180000000,3,1333684326,\"Beinwickel
		// // des
		// //
		// Lavabebens\",\"Interface\\\\Icons\\\\inv_pants_robe_raidmage_k_01\",1,4,1,85,180000000,0,200000000,0,false,\"Lacure\",0,1,71980,0,0,0,319052448
		//
		// // LINK = 1
		// String link = itemData[0];
		// // ILEVEL = 2
		// Long ilevel = Long.parseLong(itemData[1]);
		// // PRICE = 6
		// Float price = Float.parseFloat(itemData[5]);
		// // TLEFT = 7
		// Long tleft = Long.parseLong(itemData[6]);
		// // TIME = 7
		// Date time = new Date();
		// if (!itemData[7].equals("nil")) {
		// time.setTime(Long.parseLong(itemData[7]) * 1000);
		// }
		// // TIME = 8
		// String name = itemData[8];
		// // COUNT = 11
		// Long count = Long.parseLong(itemData[10]);
		// // ULEVEL = 14
		// Long ulevel = Long.parseLong(itemData[13]);
		// // MINBID = 15,
		// Float minbid = Float.parseFloat(itemData[14]);
		// // MININC = 16,
		// Float mininc = Float.parseFloat(itemData[15]);
		// // BUYOUT = 17,
		// Float buyout = Float.parseFloat(itemData[16]);
		// // CURBID = 18,
		// Float curbid = Float.parseFloat(itemData[17]);
		// // SELLER = 20
		// String seller = itemData[19];
		// // ITEMID = 23
		// Long itemid = Long.parseLong(itemData[22]);
		// // SEED = 27
		// Long seed = Long.parseLong(itemData[26]);
		//
		// new AuctionData(updated, link, ilevel, price, tleft, time, name,
		// count, ulevel, minbid, mininc, buyout, curbid, seller, itemid, seed);
		//
		// }
		// }
		//
		// renderText(dataString);
	}

	public static void showRecipeDataTable(Long id, Long sEcho, String sSearch, Integer iDisplayStart, Integer iDisplayLength, Boolean iSortingCols, Integer iSortCol_0, String sSortDir_0) {

		List<String> recipeIdsList = new ArrayList<String>();
		HashMap<Long, Integer> recipeIdCounts = new HashMap<Long, Integer>();
		RecipeShoppingCart recipeShoppingCart = null;
		recipeShoppingCart = getShoppingCart(recipeIdsList, recipeIdCounts, recipeShoppingCart);

		String order = "order by ";
		if (iSortingCols) {
			switch (iSortCol_0) {
			case 1:
				order += " r.name " + sSortDir_0 + ", ";
				break;
			case 2:
				order += " r.reagentcost " + sSortDir_0 + ", ";
				break;
			case 3:
				order += " r.profLevel " + sSortDir_0 + ", ";
				break;
			}
		}

		order += "r.item.level desc, r.reagentcost desc";

		String sql = "SELECT DISTINCT r FROM Recipe AS r,IN(r.reagents) AS rr WHERE r.profId = :id AND (:search IS NULL OR (r.name LIKE :search OR rr.item.name LIKE :search)) " + order;

		JPAQuery query = Recipe.find(sql).bind("id", id);

		if (sSearch.length() > 0) {
			sSearch = "%" + sSearch + "%";
			System.out.println(sSearch);
			query.query.setParameter("search", sSearch);
		} else {
			query.query.setParameter("search", null);
		}

		Integer filterCount = query.query.getResultList().size();

		List<Recipe> recipes = query.from(iDisplayStart).fetch(iDisplayLength);

		Long recipeCount = Recipe.count("Select DISTINCT count(*) from Recipe where profId = ?", id);

		render("/Service/showRecipeDataTable.json", recipes, sEcho, recipeCount, recipeShoppingCart, recipeIdCounts, filterCount);
	}

	private static RecipeShoppingCart getShoppingCart(List<String> recipeIdsList, HashMap<Long, Integer> recipeIdCounts, RecipeShoppingCart recipeShoppingCart) {
		Cookie savedRecipes = request.cookies.get("savedRecipes");
		if (savedRecipes == null) {
			savedRecipes = new Cookie();
			savedRecipes.value = "";
			response.setCookie("savedRecipes", "");
		}

		recipeIdsList.addAll(Arrays.asList(savedRecipes.value.split(",")));

		try {
			if (recipeIdsList.size() > 0) {
				for (String string : recipeIdsList) {
					Integer test = recipeIdCounts.get(Long.parseLong(string));
					if (test == null) {
						test = 1;
					} else {
						test = test + 1;
					}
					recipeIdCounts.put(Long.parseLong(string), test);
				}
				recipeShoppingCart = new RecipeShoppingCart(recipeIdsList);
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
		return recipeShoppingCart;
	}

	public static void showRecipe(Long id) {
		Recipe recipe = Recipe.findById(id);
		render(recipe);
	}

	public static void getRecipeCrafter(Long id) {

		List<Avatar> avatars = Avatar.find("SELECT DISTINCT ar.avatar FROM AvatarRecipe ar join ar.avatar a WHERE ar.recipe = ?", Recipe.findById(id)).fetch();
		render(avatars);
	}

	public static void update() {
		// Item item = Item.find("jsonEquip like ? and name = 'Zaubertuch'",
		// "%avgbuyout%").first();
		//
		// double teyt = item.avgbuyout;
		//
		// renderHtml(NumberUtils.formatGold(item.avgbuyout));

		// List<Recipe> recipes = Recipe.findAll();
		//
		// for (Recipe recipe : recipes) {
		// List<RecipeReagent> recipeReagents = recipe.reagents;
		// for (RecipeReagent recipeReagent : recipeReagents) {
		// recipe.reagentcost = recipe.reagentcost +
		// (recipeReagent.item.avgbuyout * recipeReagent.count);
		// }
		// System.out.println(recipe.reagentcost);
		// recipe.save();
		// }

		// List<Item> items = Item.findAll();
		// for (Item item : items) {
		// item.setAuctionData();
		// }

		// List<Recipe> recipes = Recipe.findAll();
		// for (Recipe recipe : recipes) {
		// recipe.reagentcost = 0;
		// for (RecipeReagent reagent : recipe.reagents) {
		// if (reagent.item.buyout > 0) {
		// recipe.reagentcost = recipe.reagentcost + (reagent.item.buyout *
		// reagent.count);
		// System.out.println("new data");
		// } else {
		// recipe.reagentcost = recipe.reagentcost + (reagent.item.avgbuyout *
		// reagent.count);
		// }
		// }
		// recipe.save();
		// }
		//

		// this.reagentcost = this.reagentcost + (recipeReagent.item.avgbuyout *
		// recipeReagent.count);

		// List<Item> items = Item.find("jsonEquip like ?",
		// "%avgbuyout%").fetch();
		// for (Item item : items) {
		// if (!item.jsonEquip.contains("{null}")) {
		// JsonObject oJson = new
		// JsonParser().parse(item.jsonEquip).getAsJsonObject();
		// String memberName = "avgbuyout";
		// if (oJson.has(memberName)) {
		// item.avgbuyout = oJson.get(memberName).getAsFloat();
		// System.out.println(item.name + " Auktionskosten: " + item.avgbuyout);
		// item.save();
		// }
		// }
		// }

		// List<Recipe> recipes =
		// Recipe.find("item_id is null and name is not null").fetch();
		// for (Recipe recipe : recipes) {
		// recipe.setTooltip();
		// }
	}

}
