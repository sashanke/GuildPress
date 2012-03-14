package models.wowapi.resources;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
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
    
    public String toString() {
        return name + " (" + region + ")";
    }
    
}
