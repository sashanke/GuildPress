package models.raidtracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.wowapi.character.Avatar;
import play.db.jpa.Model;

@Entity
public class Raid extends Model {
	@ManyToOne
	public RaidPool pool;

	@ManyToOne
	public Avatar offizier;

	public Date startDate;
	public Date endDate;

	@OneToMany(mappedBy = "raid", cascade = CascadeType.ALL)
	public List<RaidZones> zonen;

	@OneToMany(mappedBy = "raid", cascade = CascadeType.ALL)
	public List<RaidBossKills> bosse;

	@OneToMany(mappedBy = "raid", cascade = CascadeType.ALL)
	public List<RaidMember> member;

	@OneToMany(mappedBy = "raid", cascade = CascadeType.ALL)
	public List<RaidItem> items;

	public Raid(Long poolid, Date startDate, Date endDate, Avatar offizier) {
		this.pool = RaidPool.findById(poolid);
		this.startDate = startDate;
		this.endDate = endDate;
		this.offizier = offizier;
		this.zonen = new ArrayList<RaidZones>();
		this.bosse = new ArrayList<RaidBossKills>();
		this.member = new ArrayList<RaidMember>();
		this.items = new ArrayList<RaidItem>();
	}

}
