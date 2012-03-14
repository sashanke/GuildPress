package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.Play;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.FileUtils;

@Entity
public class Post extends Model {
	@Required
    public String title;
    
    @Required
    public Date postedAt;
    
    @Lob
    @Required
    @MaxSize(10000)
    public String content;
    
    @Required
    @ManyToOne
    public User author;
    
    public String image;
    
    public String image_frontpage;
    
    @Lob
    @MaxSize(150)
    public String post_abstract;
    
    
    @OneToMany(mappedBy="post", cascade=CascadeType.ALL)
    public List<Comment> comments;
    
    @ManyToMany(cascade=CascadeType.PERSIST)
    public Set<Tag> tags;
    
    public Post(User author, String title, String content) {
    	this.comments = new ArrayList<Comment>();
    	this.tags = new TreeSet<Tag>();
        this.author = author;
        this.title = title;
        this.content = content;
        this.image = FileUtils.getRandomFile(Play.configuration.getProperty("conf.artworksdir"));
        this.postedAt = new Date();
    }
    
    public String toString() {
        return title;
    }
    
    
    public Post previous() {
    	Post post = Post.find("postedAt < ? order by postedAt desc", postedAt).first();
//    	if (post == null ) {
//    		post = Post.find("order by postedAt asc").first();
//		}
    	
        return post;
    }
     
    public Post next() {
    	Post post = Post.find("postedAt > ? order by postedAt asc", postedAt).first();
    	
//    	if (post == null ) {
//    		post = Post.find("order by postedAt desc").first();
//		}
//    	
        return post; 
    }
    
    public Post tagItWith(String name) {
        tags.add(Tag.findOrCreateByName(name));
        return this;
    }
    
    public static List<Post> findTaggedWith(String tag) {
        return Post.find(
            "select distinct p from Post p join p.tags as t where t.name = ?", tag
        ).fetch();
    }
    
    public static List<Post> findTaggedWith(String... tags) {
        return Post.find(
                "select distinct p from Post p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.title, p.content,p.postedAt having count(t.id) = :size"
        ).bind("tags", tags).bind("size", tags.length).fetch();
    }
    
    public Post addComment(String author, String content) {
        Comment newComment = new Comment(this, author, content).save();
        this.comments.add(newComment);
        this.save();
        return this;
    }
}
