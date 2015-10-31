package implicits

import play.api.mvc.Request

object Implicits {
  implicit class RichString(s:String) {
    def toOption: Option[String] = if (s != null && s != "") Some(s) else None
  }

  implicit class RichRequest(request:Request[Any]) {
    def remoteAddressWithXRealIp: String = request.headers.get("X-Real-IP").getOrElse(request.remoteAddress)
  }
}
