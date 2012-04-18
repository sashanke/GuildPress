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
		
		String useragent = "";
		String accept = "";

		try {
			useragent = Request.current().headers.get("user-agent").value();
			accept = Request.current().headers.get("accept").value();
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		
		UAgentInfo uai = new UAgentInfo(useragent, accept);
		renderArgs.put("isIphone", uai.isTierIphone);
		
		redirect("/mobile");
	}

	public static void index() {
		render();
	}
	

}
