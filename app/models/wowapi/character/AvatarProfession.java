package models.wowapi.character;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.wowapi.resources.Icon;
import models.wowapi.resources.Recipe;
import play.db.jpa.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Entity
public class AvatarProfession extends Model {

	public Long profId;

	public String name;

	public Long rank;

	public Long max;

	@ManyToOne
	public Icon icon;

	@ManyToOne
	public Avatar avatar;

	@OneToMany(mappedBy = "avatarProfession", cascade = CascadeType.ALL)
	public List<AvatarRecipe> avatarRecipes;

	public Boolean isPrimary;

	public Date lastModified;
	public Date lastUpdate;
	public Date created;

	public AvatarProfession() {
		this.avatarRecipes = new ArrayList<AvatarRecipe>();
		if (this.created == null) {
			this.created = new Date();
		}
		this.lastModified = new Date();
		this.lastUpdate = new Date();
	}

	public static List<AvatarProfession> createProfessions(JsonObject professions, Avatar avatar) {	
		AvatarRecipe.delete("avatar = ?", avatar);
		AvatarProfession.delete("avatar = ?", avatar);
		
		List<AvatarProfession> apl = new ArrayList<AvatarProfession>();
		if (professions.has("primary") && professions.get("primary").isJsonArray()) {
			JsonArray primaryprofessions = professions.get("primary").getAsJsonArray();
			for (JsonElement professionElement : primaryprofessions) {
				apl.add(createProfession(avatar, professionElement, true));
			}
		}
		if (professions.has("secondary") && professions.get("secondary").isJsonArray()) {
			JsonArray secondaryprofessions = professions.get("secondary").getAsJsonArray();
			for (JsonElement professionElement : secondaryprofessions) {
				apl.add(createProfession(avatar, professionElement, false));
			}
		}
		return apl;
	}

	private static AvatarProfession createProfession(Avatar avatar, JsonElement professionElement, Boolean primary) {
		AvatarProfession ap = new AvatarProfession();
		JsonObject profession = professionElement.getAsJsonObject();
		ap.avatar = avatar;
		ap.profId = profession.get("id").getAsLong();
		ap.isPrimary = primary;
		ap.name = profession.get("name").getAsString();
		ap.rank = profession.get("rank").getAsLong();
		ap.max = profession.get("max").getAsLong();
		ap.icon = Icon.setIcon(ap.profId, profession.get("icon").getAsString());
		ap.save();

		if (profession.has("recipes") && profession.get("recipes").isJsonArray() && ap.isPrimary) {
			JsonArray recipes = profession.get("recipes").getAsJsonArray();
			for (JsonElement recipe : recipes) {
				AvatarRecipe avatarRecipe = AvatarRecipe.setAvatarRecipe(avatar,ap,recipe.getAsLong());
				avatarRecipe.save();
				ap.avatarRecipes.add(avatarRecipe);
				ap.save();
			}
		}
		return ap;
	}

}
