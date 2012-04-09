package models.wowapi.character.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import models.wowapi.character.Avatar;
import models.wowapi.character.AvatarProfession;
import models.wowapi.resources.Recipe;
import play.db.DB;

public class AvatarProfessionHelper {
	public static List<AvatarProfessionHelper> getList(AvatarProfession avatarProfession) {
		LinkedHashMap<Long, List<Long>> recipesPerAvatar = new LinkedHashMap<Long, List<Long>>();
		List<AvatarProfessionHelper> recipes = new ArrayList<AvatarProfessionHelper>();
		try {
			PreparedStatement ps = DB
					.getConnection()
					.prepareStatement(
							"select ap.avatar_id, re.id as recipe_id from AvatarRecipe ar join AvatarProfession ap on (ar.avatarProfession_id = ap.id) join Recipe re on (ar.recipe_id = re.id and re.name is not null) join Item im on (re.item_id = im.id and im.name is not null) where ap.profId = ? order by re.profLevel desc, im.level desc");
			ps.setLong(1, avatarProfession.profId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				Long recipeId = rs.getLong("recipe_id");
				Long avatarId = rs.getLong("avatar_id");

				if (recipesPerAvatar.containsKey(recipeId)) {
					List<Long> avatars = recipesPerAvatar.get(recipeId);
					avatars.add(avatarId);
					recipesPerAvatar.put(recipeId, avatars);
				} else {
					ArrayList<Long> avatars = new ArrayList<Long>();
					avatars.add(avatarId);
					recipesPerAvatar.put(recipeId, avatars);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Entry<Long, List<Long>> e : recipesPerAvatar.entrySet()) {
			Long recipeId = e.getKey();
			List<Long> avatarList = e.getValue();
			Recipe recipe = Recipe.findById(recipeId);
			List<Avatar> avatars = Avatar.find("id in :ids").bind("ids", avatarList).fetch();
			recipes.add(new AvatarProfessionHelper(avatarProfession, avatars, recipe));
		}

		return recipes;
	}

	AvatarProfession avatarProfession;
	List<Avatar> avatars;

	Recipe recipe;

	public AvatarProfessionHelper(AvatarProfession avatarProfession, List<Avatar> avatars, Recipe recipe) {
		this.avatarProfession = avatarProfession;
		this.avatars = avatars;
		this.recipe = recipe;
	}
}
