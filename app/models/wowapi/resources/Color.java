package models.wowapi.resources;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.db.jpa.Model;
@Entity
public class Color extends Model {
	public long orange;
	public long yellow;
	public long green;
	public long grey;
	@OneToOne
	Recipe recipe;
	public Color(Recipe recipe,long orange,long yellow,long green,long grey) {
		this.recipe = recipe;
		this.orange = orange;
		this.yellow = yellow;
		this.green = green;
		this.grey = grey;
		this.save();
	}
}
