package models

case class ShortUrl(
  id: Option[Long] = None,
  original_url: String,
  hash: Option[String] = None
)

case class Issue(
  code: Int,
  message: IssueMessage
)