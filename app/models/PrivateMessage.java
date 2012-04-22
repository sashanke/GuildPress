package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.wowapi.character.Avatar;

import play.data.validation.MaxSize;
import play.db.jpa.Model;

@Entity
public class PrivateMessage extends Model {
	
	public String subject;
	@Lob
	@MaxSize(10000)
	public String body;
	
	public Date sendDate;
	public Date readDate;
	public Boolean readed;
	
	@ManyToOne
	public Avatar fromUser;
	@ManyToOne
	public Avatar toUser;
	
	public PrivateMessage reply;
	
	public PrivateMessage(String subject, String body, Date date, Avatar from, Avatar to, PrivateMessage reply) {
		
		if (subject == null || subject.trim().length() == 0) {
			subject = "Kein Betreff";
		}
		
		this.subject = subject;
		this.body = body;
		this.sendDate = date;
		this.fromUser = from;
		this.toUser = to;
		this.reply = reply;
		this.readed = false;
	}
	@Override
	public String toString() {
		return this.subject;
	}

}
