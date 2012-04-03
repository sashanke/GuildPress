package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import models.wowapi.character.AvatarProfession;
import models.wowapi.character.helper.AvatarProfessionHelper;
import models.wowapi.resources.helper.RecipeShoppingCart;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import utils.Tools;
import flexjson.JSONSerializer;

public class Recipes extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
	
	public static void index() {
		render();
	}
	
	public static void save(Long id) {	
		Cookie savedRecipes = request.cookies.get("savedRecipes");
		if (savedRecipes == null) {
			savedRecipes = new Cookie();
			savedRecipes.value = "";
		}
		List<String> recipeIdsList = new ArrayList<String>();
		recipeIdsList.addAll(Arrays.asList(savedRecipes.value.split(",")));
		recipeIdsList.add(id.toString());
		recipeIdsList.remove("");
		response.setCookie("savedRecipes", Tools.implodeList(recipeIdsList,","));

		HashMap<Long, Integer> recipeIdCounts = new HashMap<Long, Integer>();
		RecipeShoppingCart recipeShoppingCart = null;
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
		render("Recipes/shoppingcart.html",recipeShoppingCart);
	}
	public static void remove(Long id) {	
		Cookie savedRecipes = request.cookies.get("savedRecipes");
		if (savedRecipes == null) {
			savedRecipes = new Cookie();
			savedRecipes.value = "";
			response.setCookie("savedRecipes", "");
		}
		List<String> recipeIdsList = new ArrayList<String>();
		recipeIdsList.addAll(Arrays.asList(savedRecipes.value.split(",")));
		recipeIdsList.remove(id.toString());
		recipeIdsList.remove("");
		response.setCookie("savedRecipes", Tools.implodeList(recipeIdsList,","));
		
		HashMap<Long, Integer> recipeIdCounts = new HashMap<Long, Integer>();
		RecipeShoppingCart recipeShoppingCart = null;
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
		render("Recipes/shoppingcart.html",recipeShoppingCart);
	}
	
	
	public static void showProfession(Long id, String name) {
		Cookie savedRecipes = request.cookies.get("savedRecipes");
		if (savedRecipes == null) {
			savedRecipes = new Cookie();
			savedRecipes.value = "";
			response.setCookie("savedRecipes", "");
		}
		List<String> recipeIdsList = new ArrayList<String>();
		HashMap<Long, Integer> recipeIdCounts = new HashMap<Long, Integer>();
		RecipeShoppingCart recipeShoppingCart = null;
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

		System.out.println(recipeShoppingCart);
		
		AvatarProfession profession = AvatarProfession.find("byProfId", id).first();
		
		List<AvatarProfessionHelper> recipes = AvatarProfessionHelper.getList(profession);

		
		
		
		render(profession,recipes,recipeIdCounts,recipeShoppingCart);
	}
}
