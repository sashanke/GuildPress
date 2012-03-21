package models.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Comment;
import models.News;
import models.User;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.Tools;

@Entity
public class Topic extends Model {
	@Required
	public String title;

	private String slug;

	@Lob
	@MaxSize(10000)
	public String description;

	@Required
	@ManyToOne
	public User author;

	@Required
	public Date created;

	public Date updated;

	@ManyToOne
	public Forum forum;

	@OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
	public List<Post> posts;
	
	@OneToOne
	public Post lastPost;
	
	@OneToOne
	public Post firstPost;
	
	public Long viewCount;
	
	public Long postCount;

	
	public Topic(Forum forum, User postAuthor, String description, String title) {
		this.posts = new ArrayList<Post>();
		this.author = postAuthor;
		this.forum = forum;
		this.description = description;
		this.title = title;
		this.created = new Date();
		this.slug = Tools.Slugify(title);
		this.viewCount = 0L;
		this.postCount = 1L;
	}


	public Post addPost(User postAuthor, String content, String title) {
        Post newPost = new Post(this, postAuthor, content, title).save();
        this.posts.add(newPost);
        this.lastPost = newPost;
        this.postCount = this.postCount + 1L;
        this.save();
        this.forum.postCount = this.forum.postCount + 1L;
        this.forum.save();
        return newPost;
    }
	
	public void onView() {
		this.viewCount = this.viewCount + 1L;
		this.save();
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
