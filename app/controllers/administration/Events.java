package controllers.administration;

import models.Config;
import models.Event;
import controllers.CRUD;
import controllers.Check;

@Check("admin")
@CRUD.For(Event.class)
public class Events extends CRUD {
}