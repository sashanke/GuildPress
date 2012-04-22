package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.StringUtils;

@Entity
public class Message extends Model {
	@ManyToOne
	public User user;

	public Date messageDate;

	@Lob
	@MaxSize(10000)
	private String rawMessage;

	@SuppressWarnings("unused")
	@Transient
	private String fullMessage;
	
	@SuppressWarnings("unused")
	@Transient
	private String shortMessage;
	
	public Message(String message, User user) {
		this.rawMessage = message;
		this.user = user;
		this.messageDate = new Date();
	}

	/**
	 * @return the rawMessage
	 */
	public String getRawMessage() {
		return rawMessage;
	}

	/**
	 * @param rawMessage the rawMessage to set
	 */
	public void setRawMessage(String rawMessage) {
		this.rawMessage = rawMessage;
	}

	public String getFullMessage() {
		return replace(clean(this.rawMessage));
	}
	public String getShortMessage() {
		return replace(shorten(this.rawMessage,this.id));
	}
	
	private static String shorten(String message, Long id) {
		if (message.length() > 60) {
			message = message.substring(0, 60) + " <span class=\"shoutbox-more\" rel=\"/shoutbox/message/" + id + "\">... </span>";
		}
		return message;
	}

	private static String clean(String message) {
		return Jsoup.clean(message, Whitelist.none()).trim();
	}

	private static String replace(String message) {
		message = StringUtils.replaceUrls(message, "shoutbox-url", "target=\"_new\"");
		message = StringUtils.replaceSmilies(message, "/public/images/emoticons/blacy/", "emoteicon noborder", "");
		return message;
	}

	
	
}
