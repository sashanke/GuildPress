package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import models.Config;
import models.Event;
import models.Message;
import models.News;
import models.User;
import models.forum.Category;
import models.forum.Post;
import models.forum.Topic;
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
import play.data.validation.Validation;
import play.libs.Codec;
import play.libs.Crypto;
import play.libs.Images;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Scope.Session;
import play.templates.Template;
import play.templates.TemplateLoader;
import play.vfs.VirtualFile;
import utils.FileUtils;
import utils.StringUtils;
import utils.UAgentInfo;
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
		renderArgs.put("pusherkey", Config.getConfig("pusher.key"));
		renderArgs.put("version", Play.configuration.getProperty("application.version"));
		
		List<Topic> boardTopics;
		
		if (User.checkGuildmember(session.get("username"))) {
			boardTopics = Topic.find("ORDER BY lastPost.created desc").fetch(4);
		} else {
			boardTopics = Topic.find("forum.isPublic = ? ORDER BY lastPost.created desc", true).fetch(4);
		}
		
		renderArgs.put("boardTopics", boardTopics);
		
		
		List<Event> nextEvents = null;
		
		if (User.checkGuildmember(session.get("username"))) {
			nextEvents = Event.find("? <= eventStart", new Date()).fetch(4);
		}
		
		renderArgs.put("nextEvents", nextEvents);
		
		
		List<Message> shoutMessages = Message.find("order by messageDate desc").fetch(5);
		Collections.reverse(shoutMessages);
		renderArgs.put("shoutMessages", shoutMessages);
		
		User user = User.getConnectedUser(session.get("username"));
		if (user != null) {
			user.activity();
		}

		Http.Cookie remember = request.cookies.get("rememberme");
        if(remember != null && user == null) {
            int firstIndex = remember.value.indexOf("-");
            int lastIndex = remember.value.lastIndexOf("-");
            if (lastIndex > firstIndex) {
                String sign = remember.value.substring(0, firstIndex);
                String restOfCookie = remember.value.substring(firstIndex + 1);
                String username = remember.value.substring(firstIndex + 1, lastIndex);
                String time = remember.value.substring(lastIndex + 1);
                if(Crypto.sign(restOfCookie).equals(sign)) {
                    session.put("username", username);
                }
            }
        }
		
		
		session.put("url", request.url);
		renderArgs.put("user", user);
	}

	public static void captcha(String id) {
		Images.Captcha captcha = Images.captcha();
		String code = captcha.getText("#E4EAFD");
		Cache.set(id, code, "10mn");
		renderBinary(captcha);
	}

	public static void charfetch(Long id, String name) {
		Realm realm = Realm.findById(id);
		Avatar avatar = Avatar.createAvatar(name, realm.name);
		render(avatar);
	}

	public static void guild(Long id, String name) {
		Guild guild = Guild.find("name = ?", name).first();
		render(guild);
	}

	public static void index() {
		List<Topic> topics = Topic.find("forum.isNewsBoard = ? ORDER BY created desc", true).fetch();
		render(topics);
	}


	public static void listTagged(String tag) {
		List<News> posts = News.findTaggedWith(tag);
		render(tag, posts);
	}

	public static void log(Long id) {
		Logs log = Logs.findById(id);
		render(log);
	}

	public static void postComment(Long postId, @Required(message = "Author is required") String author, @Required(message = "A message is required") String content, @Required(message = "Please type the code") String code, String randomID) {
		News post = News.findById(postId);
		validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
		if (Validation.hasErrors()) {
			render("Application/show.html", post, randomID);
		}
		post.addComment(author, content);
		flash.success("Thanks for posting %s", author);
		Cache.delete(randomID);
		show(postId);
	}

	public static void postListXml() {
		List<Topic> topics = Topic.find("forum.isNewsBoard = ? ORDER BY created desc", true).fetch(15);
		render(topics);
	}

	public static void register() {
		List<Realm> realms = Realm.all().fetch();
		render(realms);
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
			Secure.authenticate(email, password, true);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Application.index();
	}

	public static void show(Long id) {
		News post = News.findById(id);
		String randomID = Codec.UUID();
		render(post, randomID);
	}
}