package services

import models._

import scala.concurrent.Future
import com.google.inject.ImplementedBy

import scalaz._


@ImplementedBy(classOf[slickbacked.UrlShortenerServiceImpl])
trait UrlShortenerService {

  def get(hash: String): Future[IssueMessage \/ Option[ShortUrl]]
  def put(shortUrl: ShortUrl): Future[IssueMessage \/ Long]
  def update(id: Long, shortUrl: ShortUrl): Future[IssueMessage \/ Unit]
}