package models.wowapi.character;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.wowapi.resources.Recipe;
import play.db.jpa.Model;

@Entity
public class AvatarRecipe extends Model {
	
	@ManyToOne
	public Recipe recipe;

	@ManyToOne
	public Avatar avatar;
	
	@ManyToOne
	public AvatarProfession avatarProfession;

	public AvatarRecipe(Avatar avatar, AvatarProfession avatarProfession, long recipeId) {
		this.recipe = Recipe.setRecipe(recipeId, avatarProfession.profId);
		this.avatar = avatar;
		this.avatarProfession = avatarProfession;
	}

	public static AvatarRecipe setAvatarRecipe(Avatar avatar, AvatarProfession avatarProfession, long recipeId) {
		AvatarRecipe avatarRecipe = new AvatarRecipe(avatar, avatarProfession,recipeId);
		avatarRecipe.save();		
		return avatarRecipe;
	}
}
