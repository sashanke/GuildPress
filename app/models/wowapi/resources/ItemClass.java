package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * http://eu.battle.net/api/wow/data/item/classes?locale=de_DE
 * 
 * @author prime
 * 
 */
@Entity
public class ItemClass extends Model {

	public static ItemClass setItemClass(Long id, String name) {
		ItemClass ic = ItemClass.find("classId = ?", id).first();
		if (ic == null) {
			ic = new ItemClass(id, name);
			ic.save();
		}
		return ic;
	}

	public Long classId;

	public String name;
	public Date lastUpdate;

	public ItemClass(Long classId, String name) {
		this.classId = classId;
		this.name = name;
		this.lastUpdate = new Date();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
