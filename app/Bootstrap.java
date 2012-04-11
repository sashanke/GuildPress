import models.wowapi.Armory;
import models.wowapi.guild.Guild;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.CharacterSpec;
import models.wowapi.resources.Gender;
import models.wowapi.resources.GuildPerk;
import models.wowapi.resources.ItemClass;
import models.wowapi.resources.RaceClassMap;
import models.wowapi.resources.Realm;
import models.wowapi.resources.Role;
import models.wowapi.resources.Side;
import models.wowapi.resources.Skill;
import models.wowapi.resources.SkillCategorie;
import models.wowapi.resources.Source;
import play.Play;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends Job {

	@Override
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

		if (Role.findAll().size() == 0) {
			Role.createRoles();
		}
		
		if (CharacterSpec.findAll().size() == 0) {
			CharacterSpec.createSpecs();
		}
		

		
		Source.createSources();
		SkillCategorie.createSkillCategories();
		Skill.createSkills();
		
		JPA.em().flush();
		JPA.em().getTransaction().commit();
		
		if (Guild.findAll().size() == 0) {
			Guild.createGuild(Play.configuration.getProperty("wowapi.guildName"), Play.configuration.getProperty("wowapi.realmName"));
		}
		
	}

}