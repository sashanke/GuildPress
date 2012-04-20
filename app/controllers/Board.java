package controllers;

import java.util.List;

import models.User;
import models.forum.Category;
import models.forum.Forum;
import models.forum.Post;
import models.forum.Topic;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Before;
import play.mvc.Controller;

public class Board extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
		renderArgs.put("fullSize", true);
	}

	public static void addPost(Long topicId, Long authorId, @Required(message = "A message is required") String content) {
		Topic topic = Topic.findById(topicId);
		User postAuthor = User.findById(authorId);
		if (Validation.hasErrors()) {
			render("Board/showTopic.html", topic);
		}
		Post newPost = topic.addPost(postAuthor, content, topic.title);
		flash.success("Thanks for posting %s", postAuthor.avatar.name);
		flash.put("newPost", topic.lastPost.id);
		showTopic(newPost.topic.getSlug(), newPost.topic.id);
	}

	public static void addTopic(Long forumId, Long authorId, @Required(message = "A message is required") String content, @Required(message = "A title is required") String title, @Required(message = "A description is required") String description, String image, String frontpageImage,
			String frontpageAbstract) {
		Forum forum = Forum.findById(forumId);
		User postAuthor = User.findById(authorId);
		if (Validation.hasErrors()) {
			render("Board/showForum.html", forum);
		}
		Topic newTopic = forum.addTopic(postAuthor, content, title, description, image, frontpageImage, frontpageAbstract);
		flash.success("Thanks for posting %s", postAuthor.avatar.name);
		showTopic(newTopic.getSlug(), newTopic.id);
	}

	public static void index() {
		List<Category> categories;

		if (User.checkGuildmember(session.get("username"))) {
			categories = Category.find("order by position asc").fetch();
		} else {
			categories = Category.find("isPublic = ? order by position asc", true).fetch();
		}
		render(categories);
	}

	public static void showCategory(String slug, Long id) {
		Category category;

		if (User.checkGuildmember(session.get("username"))) {
			category = Category.find("id = ? order by position asc", id).first();
		} else {
			category = Category.find("id = ? and isPublic = ? order by position asc", id, true).first();
		}

		render(category);
	}

	public static void showForum(String slug, Long id) {
		Forum forum;

		if (User.checkGuildmember(session.get("username"))) {
			forum = Forum.find("id = ? order by position asc", id).first();
		} else {
			forum = Forum.find("id = ? and isPublic = ? order by position asc", id, true).first();
		}
		
		if (forum != null && (forum.isPublic || User.checkGuildmember(session.get("username")))) {
			render(forum);
		} else {
			redirect("/login");
		}
		
		
	}

	public static void showPost(String slug, Long id) {
		Post post = Post.findById(id);
		render(post);
	}

	public static void showTopic(String slug, Long id) {
		Topic topic = Topic.findById(id);
		topic.onView();
		
		if (topic.forum.isPublic || User.checkGuildmember(session.get("username"))) {
			render(topic);
		} else {
			redirect("/login");
		}
		
		
	}
}
