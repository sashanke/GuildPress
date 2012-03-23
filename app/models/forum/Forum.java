package models.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.User;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.Tools;

@Entity
public class Forum extends Model {
	@Required
	public String title;

	private String slug;

	@Required
	public Long position;

	@Required
	public Boolean isPublic;

	@Required
	public Boolean isNewsBoard;
	
	@Lob
	@MaxSize(10000)
	public String description;

	@ManyToOne
	public Category category;

	@OneToMany(mappedBy = "forum", cascade = CascadeType.ALL)
	public List<Topic> topics;

	@ManyToOne
	public Post lastPost;

	@ManyToOne
	public Topic lastTopic;
	
	public Long topicCount;
	public Long postCount;
	
	public Forum() {
		this.topics = new ArrayList<Topic>();
		this.topicCount = 0L;
		this.postCount = 0L;
	}

	public Topic addTopic(User postAuthor, String content, String title, String description,String image, String frontpageImage, String frontpageAbstract) {
		Topic newTopic = new Topic(this,postAuthor, description, title,image,frontpageImage,frontpageAbstract).save();
        Post newPost = new Post(newTopic, postAuthor, content, title).save();
        newTopic.posts.add(newPost);
        newTopic.firstPost = newPost;
        newTopic.lastPost = newPost;
        newTopic.save();
        this.lastPost = newPost;
        this.lastTopic = newTopic;
        this.topics.add(newTopic);
        this.topicCount = this.topicCount + 1L;
        this.postCount = this.postCount + 1L;
        this.save();
        return newTopic;
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
