package controllers.administration;

import models.forum.Post;
import controllers.CRUD;
import controllers.Check;
import controllers.CRUD.For;

@Check("admin")
@CRUD.For(Post.class)
public class Posts extends CRUD {
}