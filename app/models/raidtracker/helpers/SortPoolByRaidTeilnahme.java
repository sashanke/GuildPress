package models.raidtracker.helpers;

public class SortPoolByRaidTeilnahme extends RaidPoolHelper {

	public SortPoolByRaidTeilnahme(RaidPoolHelper rph) {
		super(rph);
	}

	@Override
	public int compareTo(Object o1) {
		if (this.raidprozent == ((RaidPoolHelper) o1).raidprozent) {
			return 0;
		} else if ((this.raidprozent) < ((RaidPoolHelper) o1).raidprozent) {
			return 1;
		} else {
			return -1;
		}
	}

}
