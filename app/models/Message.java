package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Message extends Model {

    public String name;
    
    @ManyToOne
    public User user;
    
    @Lob
    @Required
    @MaxSize(1000)
    public String message;
    
    public Date date;
    public Long msg_date;
    
    @Lob
    @MaxSize(10000)
	public String raw_message;

    public Message(String name, String message, User user) {
    	this.name = name;
    	this.message = message;
    	this.user = user;
    	this.date = new Date();
    	this.msg_date = new Date().getTime();
    }

    
}
