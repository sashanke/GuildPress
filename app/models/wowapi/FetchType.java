package models.wowapi;

public enum FetchType {
	WOWHEAD, BATTLENET, API;
	
	public String toURLName() {
		String s = super.toString();
		return s.toLowerCase();
	}
	
	@Override
	public String toString() {
		// only capitalize the first letter
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}

}
