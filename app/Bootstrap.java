import java.util.Date;
import java.util.List;

import play.*;
import play.jobs.*;
import play.test.*;
 
import models.*;
import models.wowapi.Armory;
import models.wowapi.guild.Guild;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.Gender;
import models.wowapi.resources.GuildPerk;
import models.wowapi.resources.Item;
import models.wowapi.resources.ItemClass;
import models.wowapi.resources.RaceClassMap;
import models.wowapi.resources.Realm;
import models.wowapi.resources.Recipe;
import models.wowapi.resources.Side;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
		if (Side.findAll().size() == 0) {
			new Side(new Long(0), "alliance").save();
			new Side(new Long(1), "horde").save();
		}

		if (Gender.findAll().size() == 0) {
			new Gender(new Long(0), "male").save();
			new Gender(new Long(1), "female").save();
		}
        
		if (Realm.findAll().size() == 0) {
			Armory.setRealms();
		}
		
		if (CharacterClass.findAll().size() == 0) {
			Armory.setCharacterClasses();
		}
		
		if (CharacterRace.findAll().size() == 0) {
			Armory.setCharacterRaces();
		}
		if (GuildPerk.findAll().size() == 0) {
			Armory.setGuildPerks();
		}
		
		if (ItemClass.findAll().size() == 0) {
			Armory.setItemClasses();
		}

		if (RaceClassMap.findAll().size() == 0) {
			RaceClassMap.createMap();
		}
		
		if (Guild.findAll().size() == 0) {
			Guild.createGuild(Play.configuration.getProperty("wowapi.guildName"), Play.configuration.getProperty("wowapi.realmName"));
		}
		
		
//		List<Recipe> recipes = Recipe.find("lastUpdate between ? and ?",new Date(new Date().getTime() - 1900000L),new Date()).fetch();
//
//		for (Recipe recipe : recipes) {
//			recipe.setTooltip();
//		}
		
//		Item.setItem(3371L); //Kristallphiole
//		Item.setItem(52980L); //Makelloser Balg
//		Item.setItem(3819L); //Drachenzahn
		
    }
 
}