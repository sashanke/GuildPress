package models.wowapi.resources;

import play.*;
import play.data.validation.Required;
import play.db.DB;
import play.db.jpa.*;

import javax.persistence.*;

import models.wowapi.character.Avatar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Entity
public class Realm extends Model {
	
	@Required
    public String name;

	@Required
    public String region;
	public Date lastUpdate;
	
	
	public String type;
	public String population;
	public Boolean queue;
	public Boolean status;
	public String slug;
	public String battlegroup;

    public Realm(String name, String region) {
        this.name = name;
        this.region = region;
        this.lastUpdate = new Date();
    }
    
    public Realm() {
        this.lastUpdate = new Date();
    }
    
    public static Realm getGuilRealm() {
    	
    	Realm guildRealm = (Realm) find("name = ? and region = ?", Play.configuration.getProperty("guild.server"), Play.configuration.getProperty("wowapi.realmArea")).first();
    	
    	if (guildRealm == null) {
    		guildRealm = new Realm(Play.configuration.getProperty("guild.server"), Play.configuration.getProperty("wowapi.realmArea"));
    		guildRealm.save();
		}
    	return guildRealm;
    }
    
    public static Realm findByName(String name) {   	
    	try {
        	PreparedStatement ps = DB.getConnection().prepareStatement("select id from Realm where BINARY name = ?");
        	ps.setString(1, name);
        	ResultSet rs = ps.executeQuery();
        	rs.next();
        	return Realm.findById(rs.getLong("id"));
		} catch (SQLException e) {
			Logger.info("Keinen passenden Realm zu ("+name+") in der Datenbank gefunden",e);
			return null;
		}
    	
    	
    }
    
    
    
    public String toString() {
        return name + " (" + region + ")";
    }
    
}
