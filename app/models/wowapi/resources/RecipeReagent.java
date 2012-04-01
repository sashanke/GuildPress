package models.wowapi.resources;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class RecipeReagent extends Model {

	@ManyToOne
	public Recipe recipe;
	
	@ManyToOne
	public Item item;
	
	public Long count;
	
	public RecipeReagent(Recipe recipe, Item item, Long count) {
		this.recipe = recipe;
		this.item = item;
		this.count = count;
		this.save();
	}

	public static RecipeReagent setRagent(Recipe recipe, Item item, Long count) {
		return new RecipeReagent(recipe,item,count);
	}

}
