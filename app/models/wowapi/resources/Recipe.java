package models.wowapi.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import models.wowapi.ArmoryTooltip;
import models.wowapi.Type;
import models.wowapi.core.FetchSite;
import models.wowapi.core.FetchType;
import play.db.jpa.Model;
import utils.NumberUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Entity
public class Recipe extends Model {
	public static String formatGold(float costs, String type) {
		if (type.equals("json")) {
			return NumberUtils.formatGold(costs).replaceAll("\\\"", "\\\\\"");
		}
		if (type.equals("html")) {
			return NumberUtils.formatGold(costs);
		}
		return null;
	}

	static String replace(String string, String pattern, String with) {
		String newString = string.replaceAll(pattern, with);
		return newString.trim();
	}

	public static Recipe setRecipe(Long spellId, Long profId) {
		Recipe recipe = Recipe.find("spellId = ?", spellId).first();
		if (recipe == null) {
			recipe = new Recipe(spellId, profId);
			recipe.save();
		}
		return recipe;
	}

	public long profId;

	public long spellId;

	public long profLevel;
	public long level;
	public String name;

	public Date lastModified;

	public Date lastUpdate;

	public Date created;

	@ManyToOne
	public Item item;

	@ManyToOne
	public Item learnedBy;

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
	public List<RecipeReagent> reagents;

	public Boolean isEffect;

	@ManyToOne
	public Icon icon;

	@Transient
	public int recCount;

	@ManyToOne
	public ArmoryTooltip tooltip;

	@ManyToOne
	public SkillCategorie skillCategorie;

	@ManyToMany
	public List<Skill> skill;

	@ManyToMany
	public List<Source> source;

	public long points;
	public long trainingcost;

	public float reagentcost;

	@OneToOne(cascade = CascadeType.ALL)
	Color color;

	public long itemCount;

	public Recipe(Long spellId, Long profId) {
		this.reagents = new ArrayList<RecipeReagent>();
		this.spellId = spellId;
		this.profId = profId;
		if (this.created == null) {
			this.created = new Date();
		}
		this.lastModified = new Date();
		this.lastUpdate = new Date();
		this.isEffect = false;
	}

	public String getCosts(String type) {
		this.reagentcost = 0;
		for (RecipeReagent reagent : this.reagents) {
			this.reagentcost = this.reagentcost + ((reagent.item.avgbuy > 0 ? reagent.item.avgbuy : reagent.item.avgbuyout) * reagent.count);
		}

		return formatGold(this.reagentcost, type);
	}

	private void getInfo(String body) {

		Pattern pattern = Pattern.compile("(?ism)((Listview" + Pattern.quote("({") + ")template: 'spell', id: 'recipes')(.*?)(" + Pattern.quote(")") + ";)");
		Matcher matcher = pattern.matcher(body);
		if (matcher.find()) {
			String baseData = matcher.group();
			pattern = Pattern.compile("(?ism)(data: " + Pattern.quote("[{") + ")(.*?)(" + Pattern.quote("}]") + ")");
			matcher = pattern.matcher(baseData);
			if (matcher.find()) {
				JsonObject oJson = new JsonParser().parse("{" + matcher.group(2) + "}").getAsJsonObject();

				Long cat = oJson.get("cat").getAsLong();
				this.skillCategorie = SkillCategorie.find("byCat", cat).first();

				JsonArray skills = oJson.get("skill").getAsJsonArray();
				this.skill.clear();
				for (JsonElement jsonElement : skills) {
					this.skill.add((Skill) Skill.find("bySkill", jsonElement.getAsLong()).first());
					this.profId = jsonElement.getAsLong();
				}

				if (oJson.has("source")) {
					JsonArray sources = oJson.get("source").getAsJsonArray();
					this.source.clear();
					for (JsonElement jsonElement : sources) {
						this.source.add((Source) Source.find("bySource", jsonElement.getAsLong()).first());
					}
				}
				this.profLevel = oJson.get("learnedat").getAsLong();
				this.level = oJson.get("level").getAsLong();
				this.name = oJson.get("name").getAsString().substring(1);
				this.spellId = oJson.get("id").getAsLong();
				this.points = oJson.get("nskillup").getAsLong();
				if (oJson.has("trainingcost")) {
					this.trainingcost = oJson.get("trainingcost").getAsLong();
				}

				JsonArray colors = oJson.get("colors").getAsJsonArray();
				Color color = Color.find("recipe = ?", this).first();
				if (color == null) {
					this.color = new Color(this, colors.get(0).getAsLong(), colors.get(1).getAsLong(), colors.get(2).getAsLong(), colors.get(3).getAsLong());
				} else {
					color.orange = colors.get(0).getAsLong();
					color.yellow = colors.get(1).getAsLong();
					color.green = colors.get(2).getAsLong();
					color.grey = colors.get(3).getAsLong();
					color.save();
					this.color = color;
				}

				JsonArray reagents = oJson.get("reagents").getAsJsonArray();
				for (JsonElement reagent : reagents) {
					JsonArray reag = reagent.getAsJsonArray();

					RecipeReagent recipeReagent = RecipeReagent.setRagent(this, Item.setItem(reag.get(0).getAsLong()), reag.get(1).getAsLong());

					this.reagentcost = this.reagentcost + (recipeReagent.item.avgbuyout * recipeReagent.count);
					this.reagents.add(recipeReagent);
				}
				if (oJson.has("creates")) {
					JsonArray creates = oJson.get("creates").getAsJsonArray();
					this.item = Item.setItem(creates.get(0).getAsLong());
					this.itemCount = creates.get(1).getAsLong();
				} else {
					this.isEffect = true;
				}

			}
		}

		pattern = Pattern.compile("(?ism)((Listview" + Pattern.quote("({") + ")template: 'item', id: 'taught-by-item')(.*?)(" + Pattern.quote(")") + ";)");
		matcher = pattern.matcher(body);
		if (matcher.find()) {
			String baseData = matcher.group();
			pattern = Pattern.compile("(?ism)(data: " + Pattern.quote("[{") + ")(.*?)(" + Pattern.quote("}]}") + ")");
			matcher = pattern.matcher(baseData);
			if (matcher.find()) {
				// TODO OPTIONAL! List of Recipe sources
				JsonArray recipes = new JsonParser().parse("{" + matcher.group()).getAsJsonObject().get("data").getAsJsonArray();
				for (JsonElement jsonElement : recipes) {
					this.learnedBy = Item.setItem(jsonElement.getAsJsonObject().get("id").getAsLong());
				}
			}
		}

		this.save();

	}

	private void setExtraInfos() {
		String url = "http://de.wowhead.com/spell=" + this.spellId;
		FetchSite site = FetchSite.fetch(url, FetchType.WOWHEAD);
		String response = site.response;

		this.getInfo(response);
	}

	public void setTooltip() {
		this.lastModified = new Date();
		this.lastUpdate = new Date();
		this.reagents.clear();
		ArmoryTooltip.delete("recipe_id = ?", this.id);
		this.tooltip = ArmoryTooltip.createTooltip(Type.SPELL, this.spellId);
		this.icon = this.tooltip.icon;
		this.setExtraInfos();
		this.save();
	}

}
