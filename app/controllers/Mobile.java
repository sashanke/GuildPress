package controllers;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import models.Config;
import models.Message;
import models.User;
import models.forum.Category;
import models.forum.Forum;
import models.forum.Post;
import models.forum.Topic;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Crypto;
import play.modules.pusher.Pusher;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Scope.Session;
import utils.StringUtils;
import utils.UAgentInfo;

public class Mobile extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void index() {
		news();
	}
	
	public static void news() {
		List<Topic> topics = Topic.find("forum.isNewsBoard = ? ORDER BY created desc", true).fetch();
		render(topics);
	}
	
	public static void shoutbox() {
		List<Message> shouts = Message.find("order by messageDate desc").fetch(10);
		render(shouts);
	}
	
	public static void forum() {
		List<Topic> boardTopics;
		
		if (User.checkGuildmember(session.get("username"))) {
			boardTopics = Topic.find("ORDER BY lastPost.created desc").fetch(30);
		} else {
			boardTopics = Topic.find("forum.isPublic = ? ORDER BY lastPost.created desc", true).fetch(30);
		}
		
		
		List<Category> categories;

		if (User.checkGuildmember(session.get("username"))) {
			categories = Category.find("order by position asc").fetch();
		} else {
			categories = Category.find("isPublic = ? order by position asc", true).fetch();
		}
		render(boardTopics,categories);
	}
	
	public static void login() {
		render();
	}
	public static void dologin(@Required String username, String password, boolean remember) throws Throwable {
		// Check tokens
		Boolean allowed = false;

		User check = User.connect(username, password);
		
		if (check != null) {
			allowed = true;
		}
		
		if (Validation.hasErrors() || !allowed) {
			flash.keep("url");
			flash.error("secure.error");
			params.flash();
			redirect("/mobile/login");
		}
		// Mark user as connected
		session.put("username", username);
		response.setCookie("rememberme", Crypto.sign(username) + "-" + username, "30d");
		// Remember if needed
		if (remember) {
			response.setCookie("rememberme", Crypto.sign(username) + "-" + username, "30d");
		}
		
		redirect("/mobile/news");
	}
	public static void dologout() throws Throwable {
		session.clear();
		response.removeCookie("rememberme");
		flash.success("secure.logout");
		String url = flash.get("url");
		redirect("/mobile/news");
	}
	
	public static void shoutboxAddMessageMobile(String nickname, String message) {
		User user = null;
		if (Session.current().contains("username")) {
			String username = Session.current().get("username");
			user = User.getConnectedUser(username);
		}

		Message shout = new Message(message, user).save();
		Pusher pusher = new Pusher(Config.getConfig("pusher.appId"), Config.getConfig("pusher.key"), Config.getConfig("pusher.secret"));
		pusher.trigger("shoutListChannel", "newMessage", shout.id.toString());
		pusher.trigger("shoutBoxChannel", "newMessage", shout.id.toString());
		
		redirect("/mobile/shoutbox");
	}
	
	public static void addPostMobile(Long topicId, Long authorId, @Required(message = "A message is required") String content) {
		Topic topic = Topic.findById(topicId);
		User postAuthor = User.findById(authorId);
		if (Validation.hasErrors()) {
			redirect("/mobile/forum");
		}
		Post newPost = topic.addPost(postAuthor, content, topic.title);
		flash.success("Thanks for posting %s", postAuthor.avatar.name);
		flash.put("newPost", topic.lastPost.id);
		redirect("/mobile/forum");
	}
	
	public static void addTopicMobile(Long forumId, Long authorId, @Required(message = "A message is required") String content, @Required(message = "A title is required") String title, @Required(message = "A description is required") String description) {
		Forum forum = Forum.findById(forumId);
		User postAuthor = User.findById(authorId);
		if (Validation.hasErrors()) {
			redirect("/mobile/forum");
		}
		Topic newTopic = forum.addTopic(postAuthor, content, title, description, null, null, null);
		flash.success("Thanks for posting %s", postAuthor.avatar.name);
		redirect("/mobile/forum");
	}
}
