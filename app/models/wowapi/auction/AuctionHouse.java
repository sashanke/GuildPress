package models.wowapi.auction;

import java.util.List;

/**
 * Contain the auction data of a specific faction.
 * 
 * @author Valentin
 * 
 */
public class AuctionHouse {

	private List<Auction> auctions;

	public List<Auction> getAuctions() {
		return this.auctions;
	}

	public void setAuctions(List<Auction> auctions) {
		this.auctions = auctions;
	}

	@Override
	public String toString() {
		return "AuctionHouse [auctions=" + this.auctions + "]";
	}
}
