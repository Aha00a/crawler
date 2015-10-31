package logics

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Crawler(document: Document ) {
  val title = parseOg("title").getOrElse(document.title())
  val description = parseOg("description").getOrElse(document.select("body").text())
  val image = parseOg("image").getOrElse("")

  def parseOg(key:String): Option[String] = {
    val contentFromOg = document.select(s"meta[property=og:$key]").attr("content")
    if (contentFromOg != "") {
      Some(contentFromOg)
    } else {
      None
    }
  }
}

object Crawler {
  def fromUrl(url:String) = new Crawler(Jsoup.connect(url).get())
  def fromHtml(html:String) = new Crawler(Jsoup.parse(html))
}