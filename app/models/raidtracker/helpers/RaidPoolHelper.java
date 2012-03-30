package models.raidtracker.helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.raidtracker.Raid;
import models.raidtracker.RaidItem;
import models.raidtracker.RaidMember;
import models.raidtracker.RaidPool;
import models.wowapi.character.Avatar;

import play.Logger;
import play.db.DB;

public class RaidPoolHelper implements Comparable {
	
	public String name;
	public Long raidteilname;
	public Avatar avatar;
	public List<Raid> raids;
	public Float raidprozent;
	public List<RaidItem> items;
	
	public RaidPoolHelper(String name, Long pool, List<Raid> raids) {

		try {
			this.raids = new ArrayList<Raid>();
			PreparedStatement ps = DB.getConnection().prepareStatement("select r.id raidId from Raid r join RaidMember rm on (r.id = rm.raid_id) where rm.name = ? and r.pool_id = ?");
			ps.setString(1, name);
			ps.setLong(2, pool);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				this.raids.add((Raid)Raid.findById(rs.getLong("raidId")));
			}

			
			this.items = new ArrayList<RaidItem>();
			ps = DB.getConnection().prepareStatement("select ri.item_id itemId from RaidMember rm join Raid r on (rm.raid_id = r.id) join RaidItem ri on (rm.id = ri.member_id) where rm.name = ? and r.pool_id = ?");
			ps.setString(1, name);
			ps.setLong(2, pool);
			rs = ps.executeQuery();
			while (rs.next()) {
				this.items.add((RaidItem)RaidItem.findById(rs.getLong("itemId")));
			}

			
			ps = DB.getConnection().prepareStatement("select distinct rm.name, count(*) raidteilname from RaidMember rm join Raid r on (rm.raid_id = r.id) where rm.name = ? and r.pool_id = ? group by name");
			ps.setString(1, name);
			ps.setLong(2, pool);
			rs = ps.executeQuery();
			while (rs.next()) {
				this.name = rs.getString("name");
				this.raidteilname = rs.getLong("raidteilname");
			}

			this.raidprozent = (float) ((float) this.raidteilname /  (float) raids.size() * 100);
			
			setCharacter(name);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public RaidPoolHelper(RaidPoolHelper rph) {
		this.avatar = rph.avatar;
		this.items = rph.items;
		this.name = rph.name;
		this.raidprozent = rph.raidprozent;
		this.raids = rph.raids;
		this.raidteilname = rph.raidteilname;
	}

	private void setCharacter(String name) {
		this.avatar = RaidMember.findByName(name).avatar;
	}

	public static List<RaidPoolHelper> getRaidPool(Long pool, List<Raid> raids) {
		List<String> mitglieder = RaidMember.find("select distinct rm.name from RaidMember rm where rm.raid.pool = ? and rm.name != 'Bank' and rm.name != 'Entzaubert' order by betreten desc", RaidPool.findById(pool)).fetch();
		
		List<RaidPoolHelper> raidPool = new ArrayList<RaidPoolHelper>();
		for (String raidMitglied : mitglieder) {
			RaidPoolHelper rt = new RaidPoolHelper(raidMitglied, pool, raids);
			raidPool.add(rt);
		}
		return raidPool;
	}

	@Override
	public int compareTo(Object o1) {
		if (this.raidprozent == ((RaidPoolHelper) o1).raidprozent)
            return 0;
        else if ((this.raidprozent) < ((RaidPoolHelper) o1).raidprozent)
            return 1;
        else
            return -1;
	}
	
}
