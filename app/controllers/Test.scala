package controllers

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import logics.Crawler
import play.api.Logger
import play.api.cache.CacheApi
import play.api.mvc._

@Singleton
class Test @Inject()(implicit cacheApi: CacheApi, system: ActorSystem) extends Controller {
  case class ExceptionEquals[T](actual:T, expect:T) extends Exception(s"\nActual=($actual)\nExpect=($expect)") {
    Logger.error(actual.toString)
    Logger.error(expect.toString)
  }


  def assertEquals[T](actual:T, expect:T) = {
    if(actual == expect) {

    } else {
      throw new ExceptionEquals(actual, expect)
    }
  }


  def assertEquals[T](actual:Seq[T], expect:Seq[T]) = {
    if(actual.isEmpty && expect.isEmpty) {

    }
    else
    {
      if(actual == expect) {

      } else {
        throw new ExceptionEquals(actual, expect)
      }
    }
  }

  def assertTrue(b:Boolean) = {
    if(b) {

    } else {
      throw new ExceptionEquals(b, true)
    }
  }


  def unit = Action { implicit request =>
    {
      val crawler = Crawler.fromHtml(
        """<html>
          |<head>
          |  <title>title</title>
          |</head>
          |<body>body</body>
          |</html>
        """.stripMargin)
      assertEquals(crawler.title, "title")
      assertEquals(crawler.description, "body")
      assertEquals(crawler.image, "")
    }
    {
      val crawler = Crawler.fromHtml(
        """<html>
          |<head>
          |  <title>title</title>
          |  <meta property="og:title" content="ogTitle">
          |  <meta property="og:description" content="ogDescription">
          |  <meta property="og:image" content="ogImage">
          |</head>
          |<body>body</body>
          |</html>
        """.stripMargin)
      assertEquals(crawler.title, "ogTitle")
      assertEquals(crawler.description, "ogDescription")
      assertEquals(crawler.image, "ogImage")
    }
    {
      val crawler = Crawler.fromUrl("http://people.search.naver.com/search.naver?where=nexearch&sm=tab_ppn&query=%EB%85%B8%EB%AC%B4%ED%98%84&os=99535&ie=utf8&key=PeopleService")
      assertEquals(crawler.title, "노무현 전 대통령")
      assertEquals(crawler.description, "출생-사망 1946년 9월 1일, 경상남도 김해 - 2009년 5월 23일 가족 배우자 권양숙 , 아들 노건호, 딸 노정연, 형 노건평 관련정보 역대 대한민국 대통령 사이트 공식홈페이지, 블로그, 트위터, 페이스북, 노무현사료관")
      assertEquals(crawler.image, "https://search.pstatic.net/common?type=a&size=120x150&quality=95&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F30%2F201607061043114301.jpg")
    }


    Ok("Ok.")
  }

}




