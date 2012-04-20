package controllers.administration;

import models.News;
import controllers.CRUD;

@CRUD.For(News.class)
public class NewsPost extends CRUD {
}