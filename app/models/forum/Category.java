package models.forum;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.Tools;

@Entity
public class Category extends Model {
	@Required
	public String title;

	private String slug;
	
	@Required
	public Long position;

	@Required
	public Boolean isPublic;

	@Lob
	@MaxSize(10000)
	public String description;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Forum> forums;
	
	public List<Forum> getForums() {
		return Forum.find("category = ? order by position asc", this).fetch();
	}
	
	@Override
	public String toString() {
		if (this.title != null) {
			return this.title;
		} else {
			return super.toString();
		}
	}
	
	public String getSlug() {
		if (this.slug == null) {
			this.slug = Tools.Slugify(this.title);
			this.save();
		}
		return slug;
	}
	
}
