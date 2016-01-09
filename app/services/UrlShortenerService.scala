package services

import models._

import scala.concurrent.Future
import com.google.inject.ImplementedBy

import scalaz._


@ImplementedBy(classOf[slickbacked.UrlShortenerServiceImpl])
trait UrlShortenerService {

  type EitherIssueShortUrl = Future[IssueMessage \/ Option[ShortUrl]]

  def get(hash: String): EitherIssueShortUrl
  def put(shortUrl: ShortUrl): EitherIssueShortUrl
}