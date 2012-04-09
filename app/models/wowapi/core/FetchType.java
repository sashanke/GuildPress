package models.wowapi.core;

public enum FetchType {
	WOWHEAD, BATTLENET, API;

	@Override
	public String toString() {
		// only capitalize the first letter
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}

	public String toURLName() {
		String s = super.toString();
		return s.toLowerCase();
	}

}
