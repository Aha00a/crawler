package implicits

import play.api.mvc.Request

object Implicits {
  implicit class RichString(s:String) {
    def isNullOrEmpty: Boolean = s == null || s.isEmpty
    def toOption: Option[String] = if (s.isNullOrEmpty) None else Some(s)
  }

  implicit class RichOption[T](s:T) {
    def toOption: Option[T] = Option(s)
  }


  implicit class RichRequest(request:Request[Any]) {
    def remoteAddressWithXRealIp: String = request.headers.get("X-Real-IP").getOrElse(request.remoteAddress)
  }
}
