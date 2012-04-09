/**
 * 
 */
package models.wowapi.types;

/**
 * @author prime
 * 
 */
public enum Faction {
	ALLIANCE(0), HORDE(1);

	private int faction;

	private Faction(int faction) {
		this.faction = faction;
	}

	public int getFaction() {
		return this.faction;
	}

}
