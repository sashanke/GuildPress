package models.wowapi.resources;

import java.util.HashMap;

import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class ItemLayout extends Model {
	
	public String itemSlotName;
	public Long itemOrder;
	public Long itemPlace;
	public String itemSide;
	public String itemTextSide;
	public Boolean isLeft;
	public Boolean isRight;
	public Boolean isBottom;
	public ItemLayout(String itemSlotName, Long itemOrder, Long itemPlace, String itemSide,String itemTextSide) {
		this.itemSlotName = itemSlotName;
		this.itemOrder = itemOrder;
		this.itemPlace = itemPlace;
		this.itemSide = itemSide;
		this.itemTextSide = itemTextSide;
	}
}
