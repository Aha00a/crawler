package logics

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Crawler(document: Document) {


  val (title, description, image) = (
    selectOg("title").orElse(selectText(".profile_dsc .who")).getOrElse(document.title()),
    selectOg("description").orElse(selectText(".profile_dsc .dsc")).getOrElse(document.body().text()),
    selectOg("image").orElse(selectAttr(".profile_wrap .thmb_wrap .thmb .thmb_img", "src")).getOrElse("")
    )

  def wrapOption(text: String): Option[String] = {
    if (text != null && text != "")
      Some(text)
    else
      None
  }

  def selectOg(key: String): Option[String] = {
    wrapOption(document.select(s"meta[property=og:$key]").attr("content"))
  }


  def selectText(selector: String): Option[String] = {
    wrapOption(document.select(selector).text())
  }

  def selectAttr(selector: String, attr: String): Option[String] = {
    wrapOption(document.select(selector).attr(attr))
  }
}

object Crawler {
  def fromUrl(url: String) = new Crawler(Jsoup.connect(url).get())

  def fromHtml(html: String) = new Crawler(Jsoup.parse(html))
}