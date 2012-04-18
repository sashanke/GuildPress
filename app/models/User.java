package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.forum.Post;
import models.wowapi.character.Avatar;
import models.wowapi.resources.GuildRank;
import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	public static Boolean checkGuildmember(String email) {
		User user = find("byEmail", email).first();

		if (user == null || !user.isGuildMember) {
			return false;
		}
		return true;
	}

	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}

	public static User getConnectedUser(String email) {
		return find("byEmail", email).first();
	}

	public static User getWoWCharacterUser(String name) {
		return find("user.wowCharacter.name", name).first();
	}

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

	@OneToMany
	public Set<Avatar> alts;
	
	public Date lastActiveTime;

	public User() {
		this.alts = new HashSet<Avatar>();
	}

	public User(String email, String password, String fullname) {
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		this.alts = new HashSet<Avatar>();
	}

	public void activity() {
		this.lastActiveTime = new Date();
		this.save();
	}

	public Long getPostCount() {
		return Post.count("byAuthor", this);
	}

	@Override
	public String toString() {
		return this.email;
	}
}
