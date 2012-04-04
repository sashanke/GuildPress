package models.wowapi.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.wowapi.ArmoryTooltip;
import models.wowapi.FetchType;
import models.wowapi.Type;
import models.wowapi.guild.GuildEmblem;

import play.Logger;
import play.db.jpa.Model;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import utils.FetchSite;
import utils.StringUtils;

@Entity
public class Recipe extends Model {
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

	public static Recipe setRecipe(Long spellId, Long profId) {
		Recipe recipe = Recipe.find("spellId = ?", spellId).first();
		if (recipe == null) {
			recipe = new Recipe(spellId, profId);
			recipe.save();
		}
		return recipe;
	}

	public void setTooltip() {
		this.lastModified = new Date();
		this.lastUpdate = new Date();
		this.reagents.clear();
		RecipeReagent.delete("recipe_id = ?", this.id);
		this.tooltip = ArmoryTooltip.createTooltip(Type.SPELL, this.spellId);
		this.icon = this.tooltip.icon;
		this.setExtraInfos();
		this.save();
	}

	private void setExtraInfos() {
		String url = "http://de.wowhead.com/spell=" + this.spellId;
		FetchSite site = FetchSite.fetch(url, FetchType.WOWHEAD);
		String response = site.response;

		getInfo(response);
		// this.item = getCreatedItem(response);

		// Pattern pattern =
		// Pattern.compile("(?ism)(<h3>Reagenzien</h3>)(.*?)(<table class=\"iconlist\">)(.*?)(</table>)");
		// Matcher matcher = pattern.matcher(response);
		// Pattern countPattern = Pattern.compile("(?ism)(" + Pattern.quote("(")
		// + ")([0-9]*?)(" + Pattern.quote(")") + ")");
		//
		// if (matcher.find()) {
		// pattern =
		// Pattern.compile("(?ism)(<a href=\"/item=)([0-9]*?)(\">)(.*?)</td>");
		// matcher = pattern.matcher(matcher.group(4));
		// while (matcher.find()) {
		//
		// Matcher countMatcher = countPattern.matcher(matcher.group());
		// Long count = 1L;
		// if (countMatcher.find()) {
		// count = Long.parseLong(countMatcher.group(2));
		// }
		// this.reagents.add(RecipeReagent.setRagent(this,
		// Item.setItem(Long.parseLong(matcher.group(2))), count));
		// this.save();
		// }
		//
		//
		//
		// }
	}

	// private Item getCreatedItem(String body) {
	// Pattern pattern =
	// Pattern.compile("(?ism)((Create Tradeskill Item)|(Create Item))(.*?)(<a href=\"/item=)(.*?)(\">)(.*?)(</span>)");
	// Matcher matcher = pattern.matcher(body);
	//
	// if (matcher.find()) {
	// return Item.setItem(Long.parseLong(matcher.group(6)));
	// } else {
	// this.isEffect = true;
	// pattern =
	// Pattern.compile("(?ism)(template: 'item', id: 'taught-by-item')(.*?)(\"id\":)(.*?)(,)");
	// matcher = pattern.matcher(body);
	// if (matcher.find()) {
	// return Item.setItem(Long.parseLong(matcher.group(4)));
	// }
	// }
	//
	// return null;
	// }

	private void getInfo(String body) {

		// Pattern pattern =
		// Pattern.compile("(?ism)(<table class=\"infobox\">)(.*?)(<div class=\"text\">)");
		// Matcher matcher = pattern.matcher(body);
		//
		// String infoBox = "";
		//
		// while (matcher.find()) {
		// Pattern infoPattern = Pattern.compile("(?ism)(" +
		// Pattern.quote("[ul]") + ")(.*?)(" + Pattern.quote("[/ul]") + ")");
		// Matcher infoMatcher = infoPattern.matcher(matcher.group());
		// if (infoMatcher.find()) {
		// String infos = infoMatcher.group();
		// infoBox = replace(infos, "(" + Pattern.quote("[") + ")", "<");
		// infoBox = replace(infoBox, "(" + Pattern.quote("]") + ")", ">");
		//
		// String test = "(?i)(<color=)(.*?)(>)(.*?)(</color>)";
		// infoBox = infoBox.replaceAll(test, "<span class=\"$2\">$4</span>");
		//
		// Pattern profPattern =
		// Pattern.compile("(?ism)(<li>)(Benötigt )(.*?)(</li>)");
		// Matcher profMatcher = profPattern.matcher(infoBox);
		// if (profMatcher.find()) {
		// String professionInfo = profMatcher.group(3);
		// Pattern countPattern = Pattern.compile("(?ism)(" + Pattern.quote("(")
		// + ")(.*?)(" + Pattern.quote(")") + ")");
		// Matcher countMatcher = countPattern.matcher(professionInfo);
		// if (countMatcher.find()) {
		// this.profLevel = Long.parseLong(countMatcher.group(2));
		// }
		// professionInfo = professionInfo.replaceAll("(?ism)(" +
		// Pattern.quote("(") + ")(.*?)(" + Pattern.quote(")") + ")", "");
		// this.profName = professionInfo.trim();
		// }
		// }
		// }

		// new Listview({template: 'spell', id: 'recipes', name:
		// LANG.tab_recipes, tabs: tabsRelated, parent: 'lkljbjkb574', sort:
		// ['skill', 'name'], hiddenCols: ['slot'], visibleCols: ['source'],
		// data:
		// [{"cat":11,"colors":[40,90,110,130],"id":7426,"learnedat":40,"level":0,"name":"@Brust - Schwache Absorption","nskillup":1,"reagents":[[10940,2],[10938,1]],"schools":1,"skill":[333],"source":[6],"trainingcost":100}]});

		Pattern pattern = Pattern.compile("(?ism)((Listview" + Pattern.quote("({") + ")template: 'spell', id: 'recipes')(.*?)(" + Pattern.quote(")") + ";)");
		Matcher matcher = pattern.matcher(body);
		if (matcher.find()) {
			String baseData = matcher.group();
			pattern = Pattern.compile("(?ism)(data: " + Pattern.quote("[{") + ")(.*?)(" + Pattern.quote("}]") + ")");
			matcher = pattern.matcher(baseData);
			if (matcher.find()) {
				JsonObject oJson = new JsonParser().parse("{" + matcher.group(2) + "}").getAsJsonObject();
				System.out.println(oJson);

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
					this.reagents.add(RecipeReagent.setRagent(this, Item.setItem(reag.get(0).getAsLong()), reag.get(1).getAsLong()));
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

		this.save();

	}

	static String replace(String string, String pattern, String with) {
		String newString = string.replaceAll(pattern, with);
		return newString.trim();
	}

}
