package lv.id.jc.http4s

case class Director(firstName: String, lastName: String) {
  override def toString: String = s"$firstName $lastName"
}
