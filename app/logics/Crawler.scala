package logics

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Crawler(document: Document) {


  val (title, description, image) = (
    selectOg("title")       orElse selectText(".profile_dsc .who")                                  getOrElse document.title(),
    selectOg("description") orElse selectText(".profile_dsc .dsc")                                  getOrElse document.body().text(),
    selectOg("image")       orElse selectAttr(".profile_wrap .thmb_wrap .thmb .thmb_img", "src")    getOrElse ""
  )

  def selectOg(key: String): Option[String] = document.select(s"meta[property=og:$key]").attr("content").toOption
  def selectText(selector: String): Option[String] = document.select(selector).text().toOption
  def selectAttr(selector: String, attr: String): Option[String] = document.select(selector).attr(attr).toOption
  
  implicit class RichString(s:String) {
    def toOption: Option[String] = if (s != null && s != "") Some(s) else None
  }
}

object Crawler {
  def fromUrl(url: String) = new Crawler(Jsoup.connect(url).get())

  def fromHtml(html: String) = new Crawler(Jsoup.parse(html))
}