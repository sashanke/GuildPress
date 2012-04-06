package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;

import models.wowapi.resources.Item;
import play.db.jpa.Model;
@Entity
public class AuctionData extends Model {
	@ManyToOne
	public Item item;
	//LINK = 1
	String link;
	//ILEVEL = 2
	Long ilevel;
	//PRICE = 6
	Float price;
	//TLEFT = 7
	Long tleft;
	//TIME = 7
	Date time;
	//TIME = 8
	@Index(name="idx_item_name")
	String name;
	//COUNT  = 11
	Long count;
	//ULEVEL  = 14
	Long ulevel;
	//MINBID = 15,
	Float minbid;
	//MININC = 16,
	Float mininc;
	//BUYOUT = 17,
	Float buyout;
	//CURBID = 18,
	Float curbid;
	//SELLER = 20
	@Index(name="idx_seller")
	String seller;
	//ITEMID = 23
	@Index(name="idx_itemid")
	Long itemid;
	//SEED = 27
	Long seed;
	@Index(name="idx_updated")
	Date updated;
	
	public AuctionData(Date updated, String link, Long ilevel, Float price, Long tleft, Date time, String name, Long count, Long ulevel, Float minbid, Float mininc, Float buyout, Float curbid, String seller, Long itemid, Long seed) {
		this.link = link;
		this.ilevel = ilevel;
		this.price = price;
		this.tleft = tleft;
		this.time = time;
		this.name = name;
		this.count = count;
		this.ulevel = ulevel;
		this.minbid = minbid;
		this.mininc = mininc;
		this.buyout = buyout;
		this.curbid = curbid;
		this.seller = seller;
		this.itemid = itemid;
		this.seed = seed;
		this.updated = updated;
		this.item = Item.setItem(this.itemid);
		this.save();
	}
}
