package controllers;
 
import play.*;
import play.mvc.*;
 
import java.util.*;
 
import models.*;

@Check("admin")
@With(Secure.class)
public class Admin extends Controller {
    
    @Before
    static void setConnectedUser() {
    	Application.addDefaults();
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user);
        }
    }
 
    public static void index() {
        render();
    }
    
}