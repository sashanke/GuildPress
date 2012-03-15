package controllers;

import play.*;
import play.mvc.*;
import play.libs.F.*;

import java.util.*;
import com.google.gson.reflect.*;

import models.*;

public class ChatRoom extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}
    public static void room(String chatname) {
        Chat.get().join(chatname);
        render(chatname);
    }
    
    public static void say(String chatname, String message, String link) {
        Chat.get().say(chatname, message, link);
    }
    
    public static void waitMessages(Long lastReceived) {        
        // Here we use continuation to suspend 
        // the execution until a new message has been received
        List messages = await(Chat.get().nextMessages(lastReceived));
        renderJSON(messages, new TypeToken<List<IndexedEvent<Chat.Event>>>() {}.getType());
    }
    
    public static void leave(String chatname) {
        Chat.get().leave(chatname);
        Application.index();
    }
    
}

