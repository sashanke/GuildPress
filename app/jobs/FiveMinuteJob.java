package jobs;

import java.util.Date;
import java.util.List;

import com.google.gson.JsonElement;

import controllers.Service;

import models.wowapi.Armory;
import models.wowapi.character.Avatar;
import models.wowapi.guild.Guild;
import models.wowapi.guild.GuildMember;
import models.wowapi.resources.Gender;
import models.wowapi.resources.Recipe;
import models.wowapi.resources.Side;
import play.Logger;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.WS;
import play.libs.F.Promise;
import play.libs.WS.HttpResponse;

@Every("5mn")
public class FiveMinuteJob extends Job {
	public void doJob() {
//		Logger.info("[FiveMinuteJob][start]");
//		List<GuildMember> member = GuildMember.find("lastUpdate < ? or avatar_id is null", new Date(new Date().getTime() - Armory.QUATERDAYUPDATE)).fetch(3);
//		for (GuildMember guildMember : member) {
//			Avatar.createAsyncAvatar(guildMember.name, guildMember.realm.name);
//		}
//		Logger.info("[FiveMinuteJob][stop]");
	}
}
