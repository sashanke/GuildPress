package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import flexjson.JSONSerializer;

import models.Config;
import models.PrivateMessage;
import models.User;
import models.wowapi.character.Avatar;
import models.wowapi.resources.Realm;
import play.modules.pusher.Pusher;
import play.mvc.Before;
import play.mvc.Controller;
import utils.StringUtils;

public class Member extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void index() {
		List<Realm> realms = Realm.all().fetch();
		render(realms);
	}

	public static void message(Long to) {
		User user = User.getConnectedUser(session.get("username"));
		List<PrivateMessage> mails = new ArrayList<PrivateMessage>();
		if (user.alts != null) {
			mails = PrivateMessage.find("toUser IN (?1)", user.alts).fetch();
		}
		
		Avatar toUser = null;
		if (to != null) {
			toUser = Avatar.findById(to);
		}
		render(toUser, mails);
	}

	public static void sendMessage(String toUser, String ccUser, String subject, String bodyText, Long authorId) {
		List<Avatar> toUsers = new ArrayList<Avatar>();
		List<Avatar> ccUsers = new ArrayList<Avatar>();
		
		if (toUser.contains(",")) {
			String[] stoUsers = toUser.split(",");
			for (String string : stoUsers) {
				toUsers.add(StringUtils.parseAvatarMail(string));
			}
		} else {
			toUsers.add(StringUtils.parseAvatarMail(toUser));
		}

		if (ccUser.length() > 0) {
			if (ccUser.contains(",")) {
				String[] sccUsers = ccUser.split(",");
				for (String string : sccUsers) {
					ccUsers.add(StringUtils.parseAvatarMail(string));
				}
			} else {
				ccUsers.add(StringUtils.parseAvatarMail(ccUser));
			}
		}
		for (Avatar user : toUsers) {
			new PrivateMessage(subject, bodyText, new Date(), User.getConnectedUser(session.get("username")).avatar, user, null).save();
		}

		for (Avatar user : ccUsers) {
			new PrivateMessage(subject, bodyText, new Date(), User.getConnectedUser(session.get("username")).avatar, user, null).save();
		}

		Pusher pusher = new Pusher(Config.getConfig("pusher.appId"), Config.getConfig("pusher.key"), Config.getConfig("pusher.secret"));
		pusher.trigger("presence-userOnlineChannel", "newPrivateMessageBroadcast", "check");
		
		message(null);
	}

	public static void delMessage(Long id) {
		PrivateMessage.delete("id", id);
		renderText("OK");
	}

	public static void addAlt(String name, Long id) {

		Avatar alt = Avatar.findById(id);
		User user = User.getConnectedUser(session.get("username"));
		user.alts.add(alt);
		user.save();

		if (session.contains("lastPage")) {
			String redirect = session.get("lastPage");
			session.remove("lastPage");
			redirect(redirect);
		}
		renderText("OK");
	}

	public static void delAlt(Long id) {
		Avatar alt = Avatar.findById(id);
		User user = User.getConnectedUser(session.get("username"));
		user.alts.remove(alt);
		user.save();
		renderText("OK");
	}

	public static void settings() {
		render();
	}

}
