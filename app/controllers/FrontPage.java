package controllers;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Request;
import utils.UAgentInfo;

public class FrontPage extends Controller {
	@Before
	static void addDefaults() {
		
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
		if (uai.detectMobileLong()) {
			redirect("/mobile");
		}
		
		Application.addDefaults();
	}

	public static void index() {
		render();
	}
	

}
