package models;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.forum.Post;
import models.wowapi.character.Avatar;
import models.wowapi.guild.GuildEmblem;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.GuildRank;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import flexjson.JSONDeserializer;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.WS;

@Entity
public class User extends Model {
	@Email
	@Required
	public String email;

	@Required
	public String password;

	public String fullname;
	
	public String first_name;
	public String last_name;
	public boolean isAdmin;
	public boolean isNewsAdmin;
	@ManyToOne
	public GuildRank guildRank;
	
	public boolean isGuildMember;
	@OneToOne
	public Avatar avatar;

	public User(String email, String password, String fullname) {
		this.email = email;
		this.password = password;
		this.fullname = fullname;
	}

	public User() {

	}
	
	public Long getPostCount() {
		return Post.count("byAuthor", this);
	}
	
	public String toString() {
		return email;
	}

	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}

	public static User getWoWCharacterUser(String name) {
		return find("user.wowCharacter.name",name).first();
	}
	
	public static User getConnectedUser(String email) {
		return find("byEmail",email).first();
	}
	
	public static Boolean checkGuildmember(String email) {
		User user =  find("byEmail",email).first();
		
		if (user == null || !user.isGuildMember) {
			return false;
		}
		return true;
	}
}
