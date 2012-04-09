package models.wowapi.resources;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * http://de.wowhead.com/item=78878&xml
 * 
 * @author prime
 * 
 */
@Entity
public class ItemSubClass extends Model {

	public static ItemSubClass setItemSubClass(Long id, String name) {
		ItemSubClass isc = ItemSubClass.find("subClassId = ?", id).first();
		if (isc == null) {
			isc = new ItemSubClass(id, name);
			isc.save();
		}
		return isc;
	}

	public Long subClassId;

	public String name;
	public Date lastUpdate;

	public ItemSubClass(Long subClassId, String name) {
		this.subClassId = subClassId;
		this.name = name;
		this.lastUpdate = new Date();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
