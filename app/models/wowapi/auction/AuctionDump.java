package models.wowapi.auction;

import models.wowapi.ApiException;
import models.wowapi.types.Faction;
import play.Logger;

/**
 * Contains the entire auction data of a realm.
 * 
 * @author Valentin
 * 
 */
public class AuctionDump {
	private AuctionHouse alliance;
	private AuctionHouse horde;
	private AuctionHouse neutral;

	public AuctionHouse get(Faction faction) throws ApiException {
		Logger.info("[AuctionDump][get] fetching " + faction + " auctions");
		if (faction.equals(Faction.ALLIANCE)) {
			return this.getAlliance();
		} else if (faction.equals(Faction.HORDE)) {
			return this.getHorde();
		}
		throw new ApiException("valid types for get() are alliance or horde");
	}

	public AuctionHouse getAlliance() {
		return this.alliance;
	}

	public AuctionHouse getHorde() {
		return this.horde;
	}

	public AuctionHouse getNeutral() {
		return this.neutral;
	}

	public void setAlliance(AuctionHouse alliance) {
		this.alliance = alliance;
	}

	public void setHorde(AuctionHouse horde) {
		this.horde = horde;
	}

	public void setNeutral(AuctionHouse neutral) {
		this.neutral = neutral;
	}

	@Override
	public String toString() {
		return "AuctionDump [alliance=" + this.alliance + ", horde=" + this.horde + ", neutral=" + this.neutral + "]";
	}
}
