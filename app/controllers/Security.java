package controllers;

import play.mvc.Http;
import models.User;
import models.wowapi.resources.CharacterSpec;

public class Security extends Secure.Security {

	static boolean authenticate(String username, String password) {
		return User.connect(username, password) != null;
	}
	
	static void onDisconnect() {
		String url = session.get("url");
        if(url == null) {
            url = "/news";
        }
        session.clear();
        response.removeCookie("rememberme");
        flash.success("secure.logout");
        redirect(url);
	}
	
	static void onAuthenticated() {
		String url = session.get("url");
        if(url == null) {
            url = "/news";
        }
        Http.Cookie recruitmentApplyCookie = request.cookies.get("recruitmentApply");
        if (recruitmentApplyCookie != null) {
        	Long recruitmentApplySpecc = Long.parseLong(recruitmentApplyCookie.value);
            if (recruitmentApplySpecc > 0L) {
            	CharacterSpec spec = CharacterSpec.findById(recruitmentApplySpecc);
            	response.removeCookie("recruitmentApply");
            	Recruitments.apply(recruitmentApplySpecc, spec.name, spec.cclass.name);
    		}
		}
        
        redirect(url);
    }
	
	static boolean check(String profile) {
	    if("admin".equals(profile)) {
	        return User.find("byEmail", connected()).<User>first().isAdmin;
	    }
	    return false;
	}
	
	
}