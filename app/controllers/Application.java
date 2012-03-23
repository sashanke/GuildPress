package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import models.Message;
import models.News;
import models.User;
import models.wowapi.Armory;
import models.wowapi.character.Avatar;
import models.wowapi.guild.Guild;
import models.wowapi.logs.Logs;
import models.wowapi.resources.GuildPerk;
import models.wowapi.resources.Realm;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope.Session;
import utils.FileUtils;
import utils.StringUtils;
import flexjson.JSONSerializer;

public class Application extends Controller {

	@Before
	static void addDefaults() {
		renderArgs.put("guildTitle", Play.configuration.getProperty("guild.title"));
		renderArgs.put("guildTag", Play.configuration.getProperty("guild.tag"));
		renderArgs.put("guildServer", Play.configuration.getProperty("guild.server"));
		renderArgs.put("confArtworksdir", Play.configuration.getProperty("conf.artworksdir"));
		renderArgs.put("guildWellcomemsg", Play.configuration.getProperty("guild.wellcomemsg"));
		renderArgs.put("realm", Realm.find("byName", Play.configuration.getProperty("guild.server")).first());
		renderArgs.put("guild", Guild.getMainGuild());
		renderArgs.put("guildPerk", GuildPerk.getGuildPerk(Guild.getMainGuild().level));
		renderArgs.put("logs", Logs.find("order by date desc").fetch(10));
		renderArgs.put("wowpet", FileUtils.getRandomFile("./public/images/pets/"));
		renderArgs.put("randomArtwork", Play.configuration.getProperty("conf.artworksdir") + FileUtils.getRandomFile("./public/images/artworks/"));
		renderArgs.put("user", User.getConnectedUser(session.get("username")));
	}
	
	public static void index() {
		News frontPost = News.find("order by postedAt desc").first();
		List<News> olderPosts = News.find("order by postedAt desc").from(1).fetch(10);
		render(frontPost, olderPosts);
	}

	public static void postListXml() {
		List<News> posts = News.find("order by postedAt desc").fetch(15);
		render(posts);
	}

	public static void register() {
		List<Realm> realms = Realm.all().fetch();
		render(realms);
	}

	public static void log(Long id) {
		Logs log = Logs.findById(id);
		render(log);
	}

	public static void shoutbox(Long time) {

		List<Message> cm;
		ArrayList<Message> acm = new ArrayList<Message>();
		if (time == 0) {
			cm = Message.find("order by date desc").fetch(4);
			acm.addAll(cm);
			Collections.reverse(acm);
			cm.removeAll(cm);
			cm.addAll(acm);
		} else {
			cm = Message.find("date > ? order by date desc", new Date(time)).fetch(1);
		}

		JSONSerializer characterSerializer = new JSONSerializer().include("date", "id", "msg_date", "message", "name", "raw_message", "user.wowCharacter.name").exclude("*").prettyPrint(true);

		renderJSON(characterSerializer.serialize(cm));
	}

	public static void shoutboxGetMessage(Long id) {
		Message cm = Message.findById(id);

		cm.raw_message = StringUtils.replaceUrls(cm.raw_message, "shoutbox-url", "target=\"_new\"");

		cm.raw_message = StringUtils.replaceSmilies(cm.raw_message, "/public/images/emoticons/blacy/", "emoteicon noborder", "");

		render(cm);
	}

	public static void shoutboxAddMessage(String nickname, String message) {
		User user = null;
		if (Session.current().contains("username")) {
			String username = Session.current().get("username");
			user = User.getConnectedUser(username);
		}

		Message cm = new Message(nickname, message, user);
		cm.save();
		message = Jsoup.clean(message, Whitelist.none()).trim();
		cm.raw_message = message;

		if (message.length() > 60) {
			message = message.substring(0, 60) + " <span class=\"shoutbox-more\" rel=\"/shoutbox/message/" + cm.id + "\">... </span>";
		}

		message = StringUtils.replaceUrls(message, "shoutbox-url", "target=\"_new\"");

		message = StringUtils.replaceSmilies(message, "/public/images/emoticons/blacy/", "emoteicon noborder", "");

		cm.message = message;
		cm.save();

		JSONSerializer characterSerializer = new JSONSerializer().include("date", "id", "msg_date", "message", "name", "raw_message", "user.wowCharacter").exclude("*");
		renderJSON(characterSerializer.serialize(cm));
	}

	public static void registerd(String first_name, String last_name, String email, String password, String password_check, String wowchar, Long wowrealm) {

		User user = User.find("avatar.name = ? and avatar.realm.id = ?", wowchar, wowrealm).first();
		Avatar wowChar = Avatar.find("name = ? and realm.id = ?", wowchar, wowrealm).first();

		if (user == null && password.equals(password_check)) {
			user = new User();
			user.first_name = first_name;
			user.last_name = last_name;
			user.fullname = first_name + " " + last_name;
			user.email = email;
			user.password = password;
			user.avatar = wowChar;

			if (wowChar.isGuildMember) {
				user.isGuildMember = wowChar.isGuildMember;
				user.guildRank = wowChar.guildMember.rank;
			}
			user.save();
		}

		try {
			SecureC.authenticate(email, password, true);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Application.index();
	}

	public static void charfetch(Long id, String name) {

		Realm realm = Realm.findById(id);
		Armory.fetchCharacter(realm.name, name);

		System.out.println(name);

		// JSONSerializer characterSerializer = new JSONSerializer();
		// renderJSON(characterSerializer.serialize(avatar));

		Avatar avatar = Avatar.find("name = ? and realm.name = ?", name, realm.name).first();
		render(avatar);
	}



	public static void guild(Long id, String name) {
		Guild guild = Guild.find("name = ?", name).first();
		render(guild);
	}

	public static void show(Long id) {
		News post = News.findById(id);
		String randomID = Codec.UUID();
		render(post, randomID);
	}

	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
		String code = captcha.getText("#E4EAFD");
		Cache.set(id, code, "10mn");
		renderBinary(captcha);
	}

	public static void listTagged(String tag) {
		List<News> posts = News.findTaggedWith(tag);
		render(tag, posts);
	}

	public static void postComment(Long postId, @Required(message = "Author is required") String author, @Required(message = "A message is required") String content, @Required(message = "Please type the code") String code, String randomID) {
		News post = News.findById(postId);
		validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
		if (validation.hasErrors()) {
			render("Application/show.html", post, randomID);
		}
		post.addComment(author, content);
		flash.success("Thanks for posting %s", author);
		Cache.delete(randomID);
		show(postId);
	}

}