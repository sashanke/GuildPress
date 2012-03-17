package models;

import java.util.*;

import play.Play;
import play.cache.Cache;
import play.db.jpa.Model;
import play.libs.*;
import play.libs.F.*;
import play.mvc.Router;
import play.mvc.Router.Route;
import play.mvc.Scope.Session;
import utils.StringUtils;

public class Chat {
    
    // ~~~~~~~~~ Let's chat! 
    
    final ArchivedEventStream<Chat.Event> chatEvents = new ArchivedEventStream<Chat.Event>(100);
    
    /**
     * For WebSocket, when a chatname join the room we return a continuous event stream
     * of ChatEvent
     */
    public EventStream<Chat.Event> join(String chatname) {
        chatEvents.publish(new Join(chatname));
        return chatEvents.eventStream();
    }
    
    /**
     * A chatname leave the room
     */
    public void leave(String chatname) {
        chatEvents.publish(new Leave(chatname));
    }
    
    /**
     * A chatname say something on the room
     */
    public void say(String chatname, String text, String link) {
        if(text == null || text.trim().equals("")) {
            return;
        }
        chatEvents.publish(new Message(chatname, text, link));
    }
    
    /**
     * For long polling, as we are sometimes disconnected, we need to pass 
     * the last event seen id, to be sure to not miss any message
     */
    public Promise<List<IndexedEvent<Chat.Event>>> nextMessages(long lastReceived) {
        return chatEvents.nextEvents(lastReceived);
    }
    
    /**
     * For active refresh, we need to retrieve the whole message archive at
     * each refresh
     */
    public List<Chat.Event> archive() {
        return chatEvents.archive();
    }
    
    // ~~~~~~~~~ Chat room events

    public static abstract class Event {
        
        final public String type;
        final public Long timestamp;
        
        public Event(String type) {
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
        
    }
    
    public static class Join extends Event {
        
        final public String chatname;
        
        public Join(String chatname) {
            super("join");
            this.chatname = chatname;
        }
        
    }
    
    public static class Leave extends Event {
        
        final public String chatname;
        
        public Leave(String chatname) {
            super("leave");
            this.chatname = chatname;
        }
        
    }
    
    public static class Message extends Event {
        
        final public String chatname;
        final public String text;
        final public String link;
        
        
        public Message(String chatname, String text, String link) {
            super("message");
            User user = null;
            
            if (Session.current().contains("username")) {
            	String username = Session.current().get("username");
            	user = User.getConnectedUser(username);
            	if (user != null && user.wowCharacter != null) {
            		link = "<a href=\"/avatar/"+user.wowCharacter.name.toLowerCase()+"\" class=\"no-tooltip user-name avatar avatar-member avatar-unverified avatar-${user.wowCharacter.cclass.name.toLowerCase()} avatar-class-wow avatar-class-wow-" + user.wowCharacter.cclass.name.toLowerCase() + " avatar-name-" + user.wowCharacter.name.toLowerCase() + "\"><span>" + user.wowCharacter.name + "</span></a>";
				} else {
					link = "<a href=\"/register\"><span>" + chatname + "</span></a>";
				}
			}
            
            this.chatname = chatname;
            this.text = StringUtils.replaceSmilies(text, "/public/images/emoticons/blacy/", "emoteicon noborder", "");
            this.link = link;

        }
        
    }
    
    // ~~~~~~~~~ Chat room factory

    static Chat instance = null;
    public static Chat get() {
        if(instance == null) {
            instance = new Chat();
        }
        return instance;
    }
    
}

