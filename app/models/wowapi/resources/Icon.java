package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;

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
	public Icon(Long iconId, String icon) {
		this.iconId = iconId;
		this.icon = icon;
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
	
	public String toString() {
		return icon;
	}
}
