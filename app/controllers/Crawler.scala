package controllers

import play.api.libs.json.{JsString, JsValue, Json}
import play.api.mvc._

class Crawler extends Controller {

  def get(q:String) = Action {
    val crawler = logics.Crawler.fromUrl(q)

    Ok(Json.toJson(Map[String, JsValue]("title" -> JsString(crawler.title), "image" -> JsString(crawler.image), "description" -> JsString(crawler.description))))
  }
}
