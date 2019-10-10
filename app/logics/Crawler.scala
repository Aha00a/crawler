package logics

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import implicits.Implicits._

class Crawler(document: Document) {
  val title: String = selectOg("title")
    .orElse(selectText(".profile_dsc .who"))
    .orElse(selectText("#printArea > div.view_con01_box > dl > dt")) // http://www.rokps.or.kr
    .orElse(document.title().toOption)
    .getOrElse("")

  val description: String = selectOg("description")
    .orElse(selectText(".profile_dsc .dsc"))
    .orElse(selectText("#printArea.view_bbstable")) // http://www.rokps.or.kr
    .orElse(document.body().text().toOption)
    .getOrElse("")

  val image: String = selectOg("image")
    .orElse(selectAttr(".profile_wrap .thmb_wrap .thmb .thmb_img", "src"))
    .orElse(selectAttr("#printArea > div.view_con01_box > div > img", "abs:src")) // http://www.rokps.or.kr
    .orElse(selectAttr("link[rel=shortcut icon]",  "abs:href"))
    .getOrElse("")

  def selectOg(key: String): Option[String] = document
    .select(s"meta[property=og:$key]")
    .attr("content").toOption

  def selectText(selector: String): Option[String] = document
    .select(selector)
    .text()
    .replaceAll("""[\s\xA0]+""", " ")
    .toOption

  def selectAttr(selector: String, attr: String): Option[String] = document
    .select(selector)
    .attr(attr)
    .toOption
}

object Crawler {
  def fromUrl(url: String) = new Crawler(Jsoup.connect(url).userAgent("crawler.aha00a.com").get())

  def fromHtml(html: String) = new Crawler(Jsoup.parse(html))
}