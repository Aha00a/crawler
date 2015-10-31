package implicits

object Implicits {
  implicit class RichString(s:String) {
    def toOption: Option[String] = if (s != null && s != "") Some(s) else None
  }
}
