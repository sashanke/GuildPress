package controllers.helper.crud;
 
import models.News;
import controllers.CRUD;
import play.*;
import play.mvc.*;
@CRUD.For(News.class)
public class NewsPost extends CRUD {    
}