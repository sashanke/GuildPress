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
import javax.persistence.Transient;

import play.Logger;
import play.db.jpa.Model;
import play.libs.WS;
import play.libs.WS.HttpResponse;

@Entity
public class Recipe extends Model {
	public long profId;
	public long spellId;

	public long profLevel;

	public String name;
	public String profName;

	public Date lastModified;
	public Date lastUpdate;
	public Date created;

	public String armoryTooltipURL;

	@ManyToOne
	public Item item;

	@Lob
	public String armoryTooltip;

	@Lob
	public String infobox;

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
	public List<RecipeReagent> reagents;
	
	public Boolean isEffect;

	@Transient
	public int recCount;
	
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
		String url = "http://eu.battle.net/wow/de/spell/" + this.spellId + "/tooltip";
		this.armoryTooltipURL = url;
		this.lastModified = new Date();
		this.lastUpdate = new Date();

		this.reagents.clear();
		RecipeReagent.delete("recipe_id = ?", this.id);

		HttpResponse hr = WS.url(url).get();
		if (hr.success()) {
			this.armoryTooltip = hr.getString();

			Pattern pattern = Pattern.compile("(?i)(<h3.*?>)(.+?)(</h3>)");
			Matcher matcher = pattern.matcher(this.armoryTooltip);

			while (matcher.find()) {
				// System.out.print("Start index: " + matcher.start());
				// System.out.print(" End index: " + matcher.end() + " ");
				// System.out.println(matcher.group(2));
				this.name = matcher.group(2);
			}
			setExtraInfos();
		}
		this.save();
	}

	private void setExtraInfos() {
		String url = "http://de.wowhead.com/spell=" + this.spellId;

		Logger.info("Fetching aditional infos from: " + url);

		HttpResponse hr = WS.url(url).get();
		if (hr.success()) {
			String body = hr.getString();

			Pattern pattern = Pattern.compile("(?ism)(<h3>Reagenzien</h3>)(.*?)(<table class=\"iconlist\">)(.*?)(</table>)");
			Matcher matcher = pattern.matcher(body);
			Pattern countPattern = Pattern.compile("(?ism)(" + Pattern.quote("(") + ")([0-9]*?)(" + Pattern.quote(")") + ")");
			
			if (matcher.find()) {				
				pattern = Pattern.compile("(?ism)(<a href=\"/item=)([0-9]*?)(\">)(.*?)</td>");
				matcher = pattern.matcher(matcher.group(4));
				while (matcher.find()) {
					
					Matcher countMatcher = countPattern.matcher(matcher.group());
					Long count = 1L;
					if (countMatcher.find()) {
						count = Long.parseLong(countMatcher.group(2));
					}
					this.reagents.add(RecipeReagent.setRagent(this, Item.setItem(Long.parseLong(matcher.group(2))), count));
					this.save();
				}
			}

			this.infobox = getInfoBox(body);
			this.item = getCreatedItem(body);

		}
	}

	private Item getCreatedItem(String body) {
		Pattern pattern = Pattern.compile("(?ism)(Create [Tradeskill Item|Item])(.*?)(<a href=\"/item=)(.*?)(\">)(.*?)(</span>)");
		Matcher matcher = pattern.matcher(body);

		if (matcher.find()) {
			return Item.setItem(Long.parseLong(matcher.group(4)));
		} else {
			this.isEffect = true;
		}

		return null;
	}

	private String getInfoBox(String body) {

		Pattern pattern = Pattern.compile("(?ism)(<table class=\"infobox\">)(.*?)(<div class=\"text\">)");
		Matcher matcher = pattern.matcher(body);

		String infoBox = "";

		while (matcher.find()) {
			Pattern infoPattern = Pattern.compile("(?ism)(" + Pattern.quote("[ul]") + ")(.*?)(" + Pattern.quote("[/ul]") + ")");
			Matcher infoMatcher = infoPattern.matcher(matcher.group());
			if (infoMatcher.find()) {
				String infos = infoMatcher.group();
				infoBox = replace(infos, "(" + Pattern.quote("[") + ")", "<");
				infoBox = replace(infoBox, "(" + Pattern.quote("]") + ")", ">");

				String test = "(?i)(<color=)(.*?)(>)(.*?)(</color>)";
				infoBox = infoBox.replaceAll(test, "<span class=\"$2\">$4</span>");

				Pattern profPattern = Pattern.compile("(?ism)(<li>)(Benötigt )(.*?)(</li>)");
				Matcher profMatcher = profPattern.matcher(infoBox);
				if (profMatcher.find()) {
					String professionInfo = profMatcher.group(3);
					Pattern countPattern = Pattern.compile("(?ism)(" + Pattern.quote("(") + ")(.*?)(" + Pattern.quote(")") + ")");
					Matcher countMatcher = countPattern.matcher(professionInfo);
					if (countMatcher.find()) {
						this.profLevel = Long.parseLong(countMatcher.group(2));
					}
					professionInfo = professionInfo.replaceAll("(?ism)(" + Pattern.quote("(") + ")(.*?)(" + Pattern.quote(")") + ")", "");
					this.profName = professionInfo.trim();
				}
			}
		}

		return infoBox;
	}

	static String replace(String string, String pattern, String with) {
		String newString = string.replaceAll(pattern, with);
		return newString.trim();
	}

	static String replaceWhiteSpaces(String string) {
		String pattern = "([\\n|\\r|\\t])";
		String newString = string.replaceAll(pattern, "");
		return newString.trim();
	}

}
