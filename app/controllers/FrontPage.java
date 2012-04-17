package controllers;

import java.util.ArrayList;
import java.util.List;

import controllers.SecureC.Security;

import models.Message;
import models.User;
import models.forum.Topic;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import utils.UAgentInfo;

public class FrontPage extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
	@Before
	static void setFormat() {		
		String useragent = "";
		String accept = "";

		try {
			useragent = Request.current().headers.get("user-agent").value();
			accept = Request.current().headers.get("accept").value();
		} catch (NullPointerException e) {
			// TODO: handle exception
		}

		renderArgs.put("header.user-agent", useragent);
		renderArgs.put("header.accept", accept);

		UAgentInfo uai = new UAgentInfo(useragent, accept);
		renderArgs.put("isIphone", uai.isTierIphone);
		
		System.out.println(uai.detectMobileLong());
		
		if (uai.detectMobileLong()) {
			request.format = "mobile";
		}
		response.setHeader("Content-Type", "text/html; charset=utf-8");
	}
	public static void index() {
		List<Message> shouts = Message.find("order by date desc").fetch(10);
		List<Topic> topics = Topic.find("forum.isNewsBoard = ? ORDER BY created desc", true).fetch();
		render(topics,shouts);
	}
	
	public static void login(@Required String username, String password, boolean remember) throws Throwable {
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
			redirect("/#shoutbox");
		}
		// Mark user as connected
		session.put("username", username);
		// Remember if needed
		if (remember) {
			response.setCookie("rememberme", Crypto.sign(username) + "-" + username, "30d");
		}
		
		redirect("/#shoutbox");
	}

	public static void logout() throws Throwable {
		session.clear();
		response.removeCookie("rememberme");
		flash.success("secure.logout");
		String url = flash.get("url");
		redirect("/");
	}
}
