package models.raidtracker;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.wowapi.resources.Item;
import play.db.jpa.Model;
import flexjson.JSON;

@Entity
public class RaidItem extends Model {

	public String name;

	public Date time;

	@ManyToOne
	public RaidMember member;

	public Long itemId;
	public Long cost;

	@ManyToOne
	public RaidBossKills boss;

	@ManyToOne
	public Raid raid;

	@SuppressWarnings("unused")
	private String memberName;
	@SuppressWarnings("unused")
	private String formatedDate;

	@ManyToOne
	public Item item;

	public RaidItem(String name, Date time, RaidMember member, Long itemId, Long cost, RaidBossKills boss, Raid raid, Item item) {
		this.name = name;
		this.time = time;
		this.member = member;
		this.itemId = itemId;
		this.cost = cost;
		this.boss = boss;
		this.raid = raid;
		this.item = item;
	}

	public Item checkItem() {
		return Item.setItem(this.itemId);
	}

	@JSON
	public String getFormatedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		return sdf.format(this.time);
	}

	@JSON
	public String getMemberName() {
		return this.member.name;
	}
}
