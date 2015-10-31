package logics

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Crawler(document: Document ) {



  val title = parseOg("title").orElse(parseTitle()).getOrElse(document.title())
  val description = parseOg("description").orElse(parseDescription()).getOrElse(document.body().text())
  val image = parseOg("image").orElse(parseImage()).getOrElse("")

  def parseTitle(): Option[String] = {
    if(document.baseUri().startsWith("http://people.search.naver.com")) {
      Some(document.select(".profile_dsc .who").text())
    } else {
      None
    }
  }

  def parseDescription():Option[String] = {
    if(document.baseUri().startsWith("http://people.search.naver.com")) {
      Some(document.select(".profile_dsc .dsc").text())
    } else {
      None
    }
  }

  def parseImage():Option[String] = {
    if(document.baseUri().startsWith("http://people.search.naver.com")) {
      Some(document.select(".profile_wrap .thmb_wrap .thmb .thmb_img").attr("src"))
    } else {
      None
    }
  }


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