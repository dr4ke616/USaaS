package models

import play.api.libs.json._
import play.api.libs.functional.syntax._


object JsonOps {

  implicit val formatShortUrl = Json.format[ShortUrl]

  implicit val readsIssue: Reads[Issue] = (
    (__ \ "code").read[Int] and
    (__ \ "message").read[String].map(asId[String, Issue])
  )(Issue)

  implicit val writesIssue: Writes[Issue] = (
    (__ \ "code").write[Int] and
    (__ \ "message").write[String].contramap(fromId[String, Issue])
  )(unlift(Issue.unapply))
}
