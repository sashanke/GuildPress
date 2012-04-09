package models.wowapi.resources;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class ItemSlotType extends Model {
	public static ItemSlotType setItemSlotType(String type, ItemSlot slot) {
		ItemSlotType ist = ItemSlotType.find("type = ?", type).first();

		if (ist == null) {
			ist = new ItemSlotType(type, slot, (ItemLayout) ItemLayout.find("itemSlotName = ?", type).first());
			ist.save();
		}
		return ist;
	}

	public String type;
	@ManyToOne
	public ItemSlot slot;

	@ManyToOne
	public ItemLayout layout;

	public ItemSlotType(String type, ItemSlot slot, ItemLayout layout) {
		this.type = type;
		this.slot = slot;
		this.layout = layout;
	}

}
