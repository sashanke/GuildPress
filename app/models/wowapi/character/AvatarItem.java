package models.wowapi.character;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import models.wowapi.resources.Item;
import models.wowapi.resources.ItemSlotType;
import play.db.jpa.Model;

@Entity
public class AvatarItem extends Model {

	public static List<AvatarItem> getOrderedItemList(Avatar avatar) {
		return AvatarItem.find("avatar = ? order by itemSlot.layout.itemOrder asc", avatar).fetch();
	}

	public Long itemId;
	public Long transmogItemId;

	public String tooltipParams;

	@ManyToOne
	public Avatar avatar;
	@ManyToOne
	public Item item;
	@ManyToOne
	public Item transmogItem;
	@ManyToOne
	public ItemSlotType itemSlot;

	public String armoryTooltipURL;

	@Lob
	public String armoryTooltip;

}
