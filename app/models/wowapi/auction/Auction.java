package models.wowapi.auction;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import models.wowapi.types.Faction;

import org.hibernate.annotations.Index;

import play.db.jpa.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Contains all the informations on a specific auction.
 * 
 * @author Valentin
 * 
 */
@Entity
public class Auction extends Model {
	@Id
	@SerializedName("auc")
	private Long id;
	@Index(name = "idx_auction_item_itemId")
	@SerializedName("item")
	private Long itemId;
	private String owner;
	private Long bid;
	private Long buyout;
	private Long quantity;
	private String timeLeft;

	@Enumerated(EnumType.ORDINAL)
	private Faction faction;

	public long getBid() {
		return this.bid;
	}

	public long getBuyout() {
		return this.buyout;
	}

	/**
	 * @return the faction
	 */
	public Faction getFaction() {
		return this.faction;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public long getItemId() {
		return this.itemId;
	}

	public String getOwner() {
		return this.owner;
	}

	public long getQuantity() {
		return this.quantity;
	}

	public String getTimeLeft() {
		return this.timeLeft;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public void setBuyout(Long buyout) {
		this.buyout = buyout;
	}

	/**
	 * @param faction
	 *            the faction to set
	 */
	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setTimeLeft(String timeLeft) {
		this.timeLeft = timeLeft;
	}

	@Override
	public String toString() {
		return "Auction [id=" + this.id + ", itemId=" + this.itemId + ", owner=" + this.owner + ", bid=" + this.bid + ", buyout=" + this.buyout + ", quantity=" + this.quantity + ", timeLeft=" + this.timeLeft + "]";
	}
	
	/**
	 * Auctionjob
	 * 
	 */
	public static void job() {
		// TODO Auctionjob
		
	}
}
