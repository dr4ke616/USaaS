package models

case class ShortUrl(
  original_url: String,
  hash: Option[String] = None
)

case class Issue(
  code: Int,
  message: IssueMessage
)