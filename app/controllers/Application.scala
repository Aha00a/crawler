package controllers

import implicits.Implicits._
import play.api.Logger
import play.api.libs.Jsonp
import play.api.libs.json.{JsBoolean, JsString, JsValue, Json}
import play.api.mvc._

//noinspection TypeAnnotation
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
      Ok(if(callback.isNullOrEmpty) {
        json
      } else {
        Jsonp(callback, json)
      })
    }
    catch {
      case e: Exception =>
        val json = Json.toJson(Map[String, JsValue]("success" -> JsBoolean(false), "message" -> JsString(e.getMessage)))
        Forbidden(if(callback.isNullOrEmpty) {
          json
        } else {
          Jsonp(callback, json)
        })
    }
  }
}
