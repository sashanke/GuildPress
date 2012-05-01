package controllers.administration;

import models.Config;
import models.Event;
import models.EventType;
import controllers.CRUD;
import controllers.Check;

@Check("admin")
@CRUD.For(EventType.class)
public class EventTypes extends CRUD {
}