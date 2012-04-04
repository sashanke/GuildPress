package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import models.wowapi.Type;

import play.db.jpa.Model;

/**
 * http://wow.zamimg.com/images/wow/icons/large/inv_sword_1h_deathwingraid_d_01.jpg
 * @author prime
 *
 */
@Entity
public class Icon extends Model {

	public Long iconId;

	public String icon;

	public Date lastUpdate;
	
	public Type type;
	public Long typeId;
	
	@SuppressWarnings("unused")
	@Transient
	private String image;
	
	public Icon(Long iconId, String icon) {
		this.iconId = iconId;
		this.icon = icon.toLowerCase();
		this.lastUpdate = new Date();
	}

	public Icon(Type type, Long typeId, String icon) {
		this.type = type;
		this.typeId = typeId;
		this.icon = icon.toLowerCase();
		this.lastUpdate = new Date();
	}

	public static Icon setIcon(Long id, String icon) {
		Icon iq = Icon.find("iconId = ?", id).first();
		if (iq == null) {
			iq = new Icon(id, icon);
			iq.save();
		}
		return iq;
	}
	
	public static Icon setIcon(Type type, Long typeId, String icon) {
		Icon iq = Icon.find("type = ? and typeId = ?",type, typeId).first();
		if (iq == null) {
			iq = new Icon(type, typeId, icon);
			iq.save();
		}
		return iq;
	}
	
	
	/**
	 * @return the image
	 */
	public String getImage() {
		return "/public/images/static/icons/" + icon + ".png";
	}

	public String toString() {
		return icon;
	}
}
