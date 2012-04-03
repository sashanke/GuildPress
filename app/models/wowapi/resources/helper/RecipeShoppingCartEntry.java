package models.wowapi.resources.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sun.org.apache.bcel.internal.generic.NEW;

import play.db.DB;

import models.wowapi.character.Avatar;
import models.wowapi.resources.Recipe;
import models.wowapi.resources.RecipeReagent;

public class RecipeShoppingCartEntry {
	Recipe recipe;
	int count;
	HashMap<RecipeReagent, Integer> recipe_reagents = new HashMap<RecipeReagent, Integer>();
	List<Avatar> crafter = new ArrayList<Avatar>();

	public RecipeShoppingCartEntry(Long recipeId, Integer count) {
		try {
			PreparedStatement ps = DB.getConnection().prepareStatement("SELECT ap.avatar_id FROM Avatar Join AvatarRecipe ar ON (Avatar.id = ar.avatar_id) JOIN AvatarProfession ap ON (ar.avatarProfession_id = ap.id) JOIN Recipe re ON (ar.recipe_id = re.id AND re.name IS NOT NULL) WHERE re.id = ?");
			ps.setLong(1, recipeId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Long avatarId = rs.getLong("avatar_id");
				crafter.add((Avatar) Avatar.findById(avatarId));
			}

			Recipe recipe = Recipe.findById(recipeId);
			this.recipe = recipe;
			this.recipe.recCount = count;
			this.count = count;
			for (RecipeReagent reagent : recipe.reagents) {
					recipe_reagents.put(reagent, (int) (reagent.count * count));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
