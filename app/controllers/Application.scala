package controllers

import java.net.{URLDecoder, MalformedURLException}

import play.api.Logger
import play.api.libs.Jsonp
import play.api.libs.json.{JsBoolean, JsString, JsValue, Json}
import play.api.mvc._
import implicits.Implicits._

class Application extends Controller {
  def index(q: String) = Action {
    if (q.isEmpty) {
      Redirect(routes.Application.index("http://people.search.naver.com/search.naver?where=nexearch&sm=tab_ppn&query=%EB%85%B8%EB%AC%B4%ED%98%84&os=99535&ie=utf8&key=PeopleService"))
    } else {
      Ok(views.html.index(q))
    }
  }


  def get(q: String, callback: String) = Action { implicit request =>
    try {
      Logger.info(s"${request.remoteAddressWithXRealIp}\t$q")
      val crawler = logics.Crawler.fromUrl(q)
      val json = Json.toJson(Map[String, JsValue](
        "success" -> JsBoolean(true),
        "title" -> JsString(crawler.title),
        "image" -> JsString(crawler.image),
        "description" -> JsString(crawler.description))
      )
      if(callback.isNullOrEmpty) {
        Ok(json)
      } else {
        Ok(Jsonp(callback, json))
      }
    }
    catch {
      case e: Exception =>
        if(callback.isNullOrEmpty) {
          Ok(Json.toJson(Map[String, JsValue]("success" -> JsBoolean(false),"message" -> JsString(e.getMessage))))
        } else {
          Ok(Jsonp(callback, Json.toJson(Map[String, JsValue]("success" -> JsBoolean(false),"message" -> JsString(e.getMessage)))))
        }
    }
  }
}
