package controllers

import play.api.libs.json.{JsString, JsValue, Json}
import play.api.mvc._

class Application extends Controller {
  def index(q: String) = Action {
    if (q.isEmpty) {
      Redirect(routes.Application.index("http://people.search.naver.com/search.naver?where=nexearch&sm=tab_ppn&query=%EB%85%B8%EB%AC%B4%ED%98%84&os=99535&ie=utf8&key=PeopleService"))
    } else {
      Ok(views.html.index(q))
    }
  }


  def get(q: String) = Action {
    val crawler = logics.Crawler.fromUrl(q)

    Ok(Json.toJson(Map[String, JsValue](
      "title" -> JsString(crawler.title),
      "image" -> JsString(crawler.image),
      "description" -> JsString(crawler.description))
    ))
  }
}
