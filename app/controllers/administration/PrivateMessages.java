package controllers.administration;

import models.PrivateMessage;
import controllers.CRUD;
import controllers.Check;

@Check("admin")
@CRUD.For(PrivateMessage.class)
public class PrivateMessages extends CRUD {

}
