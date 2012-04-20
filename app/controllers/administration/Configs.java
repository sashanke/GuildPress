package controllers.administration;

import models.Config;
import controllers.CRUD;
import controllers.Check;

@Check("admin")
@CRUD.For(Config.class)
public class Configs extends CRUD {
}