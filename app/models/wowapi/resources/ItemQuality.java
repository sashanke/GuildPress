package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * http://eu.battle.net/api/wow/data/item/classes?locale=de_DE
 * @author prime
 *
 */
@Entity
public class ItemQuality extends Model {

	public Long qualityId;

	public String name;

	public Date lastUpdate;
	public ItemQuality(Long qualityId, String name) {
		this.qualityId = qualityId;
		this.name = name;
		this.lastUpdate = new Date();
	}

	public static ItemQuality setItemQuality(Long id, String name) {
		ItemQuality iq = ItemQuality.find("qualityId = ?", id).first();
		if (iq == null) {
			iq = new ItemQuality(id, name);
			iq.save();
		}
		return iq;
	}
	
	public String toString() {
		return name;
	}
}
