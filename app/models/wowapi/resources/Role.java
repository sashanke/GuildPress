package models.wowapi.resources;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;
@Entity
public class Role extends Model {
	public String name;
	@ManyToOne
	public Icon icon;
	
	public Role(String name, Icon icon) {
		this.name = name;
		this.icon = icon;
	}

	public static void createRoles() {
		new Role("dps",Icon.setIcon(9999001L,"dps")).save();
		new Role("tank",Icon.setIcon(9999002L,"tank")).save();
		new Role("healer",Icon.setIcon(9999003L,"healer")).save();
	}
}
