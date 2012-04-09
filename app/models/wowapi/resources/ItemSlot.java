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
public class ItemSlot extends Model {

	public static ItemSlot setItemSlot(Long id, String name) {
		ItemSlot iq = ItemSlot.find("slotId = ?", id).first();
		if (iq == null) {
			iq = new ItemSlot(id, name);
			iq.save();
		}
		return iq;
	}

	public Long slotId;

	public String name;
	public Date lastUpdate;

	public ItemSlot(Long slotId, String name) {
		this.slotId = slotId;
		this.name = name;
		this.lastUpdate = new Date();
	}

	public Long getMappedSlot() {
		if (this.slotId == 1L) {
			return 1L;
		} else {
			return this.slotId;
		}

	}

	@Override
	public String toString() {
		return this.name;
	}
}
