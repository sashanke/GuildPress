package models.wowapi;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import models.wowapi.core.FetchSite;
import models.wowapi.core.FetchType;
import models.wowapi.resources.Icon;
import play.db.jpa.Model;

@Entity
public class ArmoryTooltip extends Model {

	public static ArmoryTooltip createTooltip(Type type, Long id) {
		ArmoryTooltip armoryTooltip = ArmoryTooltip.find("type = ? and typeId = ?", type, id).first();
		if (armoryTooltip == null) {
			return new ArmoryTooltip(type, id);
		}
		return armoryTooltip;
	}

	public Type type;

	public Long typeId;

	@ManyToOne
	public Icon icon;
	public String name;
	@Lob
	public String toolTip;

	public String url;
	public Date lastModified;
	public Date lastUpdate;

	public Date created;

	public ArmoryTooltip(Type type, Long id) {
		this.type = type;
		this.typeId = id;
		if (this.created == null) {
			this.created = new Date();
		}
		this.lastModified = new Date();
		this.lastUpdate = new Date();
		this.parseTooltip();
		this.save();
	}

	public Type getType() {
		return this.type;
	}

	private void parseTooltip() {
		this.url = "http://eu.battle.net/wow/de/" + this.type.toURLName() + "/" + this.typeId + "/tooltip";
		FetchSite site = FetchSite.fetch(this.url, FetchType.BATTLENET);
		this.toolTip = site.response;

		Pattern pattern = Pattern.compile("(?i)(<h3.*?>)(.+?)(</h3>)");
		Matcher matcher = pattern.matcher(this.toolTip);

		if (matcher.find()) {
			this.name = matcher.group(2);
		}

		String urlPattern = "(?i)(http://)(.+?)(\"\\);)";
		pattern = Pattern.compile(urlPattern);
		matcher = pattern.matcher(this.toolTip);

		if (matcher.find()) {
			String icon = matcher.group(2);
			icon = icon.substring(icon.lastIndexOf("/") + 1, icon.lastIndexOf("."));
			this.icon = Icon.setIcon(this.type, this.typeId, icon);
			this.toolTip = this.toolTip.replaceAll(urlPattern, this.icon.getImage() + "$3");
		}
		this.lastModified = new Date();
		this.lastUpdate = new Date();
	}

	public void update() {
		this.parseTooltip();
		this.save();
	}

}
