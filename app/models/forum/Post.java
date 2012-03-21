package models.forum;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import models.User;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.Tools;

@Entity
public class Post extends Model {
	@Required
	public String title;

	private String slug;

	@Lob
	@MaxSize(20000)
	public String content;

	@Required
	@ManyToOne
	public User author;

	@Required
	public Date created;

	public Date updated;

	@ManyToOne
	public Topic topic;

	public Post(Topic topic, User author, String content, String title) {
		this.topic = topic;
		this.author = author;
		this.content = content;
		this.title = title;
		this.created = new Date();
		this.slug = Tools.Slugify(title);
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
