package models.raidtracker.helpers;

import java.util.List;

import models.raidtracker.Raid;

public class SortPoolByItems extends RaidPoolHelper {

	public SortPoolByItems(RaidPoolHelper rph) {
		super(rph);
	}

	@Override
	public int compareTo(Object o1) {
		if (this.items.size() == ((RaidPoolHelper) o1).items.size())
            return 0;
        else if ((this.items.size()) < ((RaidPoolHelper) o1).items.size())
            return 1;
        else
            return -1;
	}
	
}
