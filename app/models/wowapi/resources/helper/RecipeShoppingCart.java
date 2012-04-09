package models.wowapi.resources.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import models.wowapi.resources.Item;
import models.wowapi.resources.Recipe;
import models.wowapi.resources.RecipeReagent;

public class RecipeShoppingCart {
	List<RecipeShoppingCartEntry> entries = new ArrayList<RecipeShoppingCartEntry>();
	List<Recipe> recipes = new ArrayList<Recipe>();
	HashMap<Long, Item> reagents = new HashMap<Long, Item>();

	float matsCosts = 0;

	public RecipeShoppingCart(List<String> recipeIdsList) {
		HashMap<Long, Integer> recipeIdCounts = new HashMap<Long, Integer>();
		for (String string : recipeIdsList) {
			Integer test = recipeIdCounts.get(Long.parseLong(string));
			if (test == null) {
				test = 1;
			} else {
				test = test + 1;
			}
			recipeIdCounts.put(Long.parseLong(string), test);
		}

		for (Entry<Long, Integer> recipes : recipeIdCounts.entrySet()) {
			RecipeShoppingCartEntry recipeShoppingCartEntry = new RecipeShoppingCartEntry(recipes.getKey(), recipes.getValue());
			this.recipes.add(recipeShoppingCartEntry.recipe);
			this.entries.add(recipeShoppingCartEntry);
		}

		for (RecipeShoppingCartEntry entry : this.entries) {
			HashMap<RecipeReagent, Integer> recipe_reagents = entry.recipe_reagents;
			for (Entry<RecipeReagent, Integer> reagent : recipe_reagents.entrySet()) {

				RecipeReagent rg = reagent.getKey();
				int count = reagent.getValue();
				this.matsCosts = this.matsCosts + (rg.item.avgbuy * count);

				if (this.reagents.containsKey(rg.item.id)) {
					rg.item.recCount = rg.item.recCount + count;
					this.reagents.put(rg.item.id, rg.item);
				} else {
					rg.item.recCount = count;
					this.reagents.put(rg.item.id, rg.item);
				}
			}
		}
	}

	public String getCosts(String type) {
		return Recipe.formatGold(this.matsCosts, type);
	}

}
