package models;

import java.util.List;

import play.libs.F.ArchivedEventStream;
import play.libs.F.EventStream;
import play.libs.F.IndexedEvent;
import play.libs.F.Promise;
import play.mvc.Scope.Session;
import utils.StringUtils;

public class Chat {

	// ~~~~~~~~~ Let's chat!

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
				if (user != null && user.avatar != null) {
					link = "<a href=\"/avatar/" + user.avatar.name.toLowerCase() + "\" class=\"no-tooltip user-name avatar avatar-member avatar-unverified avatar-${user.wowCharacter.cclass.name.toLowerCase()} avatar-class-wow avatar-class-wow-" + user.avatar.cclass.name.toLowerCase()
							+ " avatar-name-" + user.avatar.name.toLowerCase() + "\"><span>" + user.avatar.name + "</span></a>";
				} else {
					link = "<a href=\"/register\"><span>" + chatname + "</span></a>";
				}
			}

			this.chatname = chatname;
			this.text = StringUtils.replaceSmilies(text, "/public/images/emoticons/blacy/", "emoteicon noborder", "");
			this.link = link;

		}

	}

	final ArchivedEventStream<Chat.Event> chatEvents = new ArchivedEventStream<Chat.Event>(100);

	static Chat instance = null;

	// ~~~~~~~~~ Chat room events

	public static Chat get() {
		if (instance == null) {
			instance = new Chat();
		}
		return instance;
	}

	/**
	 * For active refresh, we need to retrieve the whole message archive at each
	 * refresh
	 */
	public List<Chat.Event> archive() {
		return this.chatEvents.archive();
	}

	/**
	 * For WebSocket, when a chatname join the room we return a continuous event
	 * stream of ChatEvent
	 */
	public EventStream<Chat.Event> join(String chatname) {
		this.chatEvents.publish(new Join(chatname));
		return this.chatEvents.eventStream();
	}

	/**
	 * A chatname leave the room
	 */
	public void leave(String chatname) {
		this.chatEvents.publish(new Leave(chatname));
	}

	// ~~~~~~~~~ Chat room factory

	/**
	 * For long polling, as we are sometimes disconnected, we need to pass the
	 * last event seen id, to be sure to not miss any message
	 */
	public Promise<List<IndexedEvent<Chat.Event>>> nextMessages(long lastReceived) {
		return this.chatEvents.nextEvents(lastReceived);
	}

	/**
	 * A chatname say something on the room
	 */
	public void say(String chatname, String text, String link) {
		if (text == null || text.trim().equals("")) {
			return;
		}
		this.chatEvents.publish(new Message(chatname, text, link));
	}

}
