package controllers.administration;

import models.wowapi.resources.GuildRank;
import controllers.CRUD;
import controllers.Check;
@Check("admin")
@CRUD.For(GuildRank.class)
public class GuildRanks extends CRUD {

}
