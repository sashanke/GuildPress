package controllers;

import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import flexjson.JSONSerializer;

import models.Config;
import models.Message;
import models.User;
import models.wowapi.guild.Guild;
import play.libs.WS.HttpResponse;
import play.modules.pusher.Pusher;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope.Session;
import utils.StringUtils;

public class ShoutBox extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void index() {
		List<Message> shouts = Message.find("order by messageDate desc").fetch();
		render(shouts);
	}

	public static void addMessage(Long id, String message, Boolean fullsize) {
		User user = User.getConnectedUser(session.get("username"));
		Message shout = new Message(message, user).save();
		Pusher pusher = new Pusher(Config.getConfig("pusher.appId"), Config.getConfig("pusher.key"), Config.getConfig("pusher.secret"));
		pusher.trigger("shoutListChannel", "newMessage", shout.id.toString());
		pusher.trigger("shoutBoxChannel", "newMessage", shout.id.toString());
		render(shout, fullsize);
	}

	public static void getMessage(Long id, Boolean fullsize) {
		Message shout = Message.findById(id);
		render("ShoutBox/addMessage.html", shout, fullsize);
	}

	public static void getMessageJson(Long id) {
		Message shout = Message.findById(id);
		JSONSerializer shoutSerializer = new JSONSerializer().include("rawMessage", "shortMessage", "fullMessage", "id", "user.avatar.name", "messageDate").exclude("*").prettyPrint(true);
		renderJSON(shoutSerializer.serialize(shout));
	}
	public static void getMessageJsonList() {
		List<Message> shoutMessages = Message.find("order by messageDate desc").fetch(5);
		Collections.reverse(shoutMessages);
		render(shoutMessages);
	}
	
	public static void updateShout(Long id, String message) {
		User user = User.getConnectedUser(session.get("username"));
		Message shout = Message.findById(id);
		if (shout.user == user || user.isAdmin) {
			shout.setRawMessage(message);
			shout.save();
			Pusher pusher = new Pusher(Config.getConfig("pusher.appId"), Config.getConfig("pusher.key"), Config.getConfig("pusher.secret"));
			pusher.trigger("shoutListChannel", "updateMessage", shout.id.toString());
			pusher.trigger("shoutBoxChannel", "updateMessage", shout.id.toString());
		}
		renderJSON(id);
	}

	public static void removeShout(Long id) {
		User user = User.getConnectedUser(session.get("username"));
		Message shout = Message.findById(id);
		if (shout != null && (shout.user == user || user.isAdmin)) {
			Message.delete("id = ?", id);
			Pusher pusher = new Pusher(Config.getConfig("pusher.appId"), Config.getConfig("pusher.key"), Config.getConfig("pusher.secret"));
			pusher.trigger("shoutListChannel", "deleteMessage", shout.id.toString());
			pusher.trigger("shoutBoxChannel", "deleteMessage", shout.id.toString());
		}
		renderJSON(id);
	}

}
