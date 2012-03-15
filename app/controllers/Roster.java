package controllers;

import java.util.List;

import models.wowapi.guild.Guild;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.CharacterClass;
import models.wowapi.resources.CharacterRace;
import models.wowapi.resources.GuildRank;
import play.mvc.Before;
import play.mvc.Controller;

public class Roster extends Controller {
	@Before
	static void addDefaults() {
		Application.addDefaults();
	}

	public static void index(Long rank, Long cclass, Long race, String search) {
	
		Guild guild = Guild.find("isMainGuild = ?", true).first();
		List<GuildMember> members;
	
		String filter = "1=1 ";
		Boolean isFiltered = false;
		if (rank != null) {
			filter = filter + "and rank.rank = " + rank;
			isFiltered = true;
		}
		if (cclass != null) {
			filter = filter + "and cclass.ccId = " + cclass;
			isFiltered = true;
		}
		if (race != null) {
			filter = filter + "and race.crId = " + race;
			isFiltered = true;
		}
	
		if (search != null && search.length() != 0) {
			filter = filter + "and name like ? ";
			isFiltered = true;
		}
	
		if (isFiltered) {
	
			if (search == null || search.length() == 0) {
				members = GuildMember.find(
						filter + " order by rank.rank, name asc").fetch();
			} else {
				members = GuildMember.find(
						filter + " order by rank.rank, name asc",
						"%" + search + "%").fetch();
			}
	
		} else {
			members = GuildMember.find(
					"rank.rank != 6 order by rank.rank, name asc").fetch();
		}
	
		List<GuildRank> granks = GuildRank.find("order by rank asc").fetch();
		List<CharacterClass> cclasses = CharacterClass
				.find("order by name asc").fetch();
		List<CharacterRace> craces = CharacterRace.find("order by name asc")
				.fetch();
		render(members, guild, granks, cclasses, craces);
	}
	
	
}
