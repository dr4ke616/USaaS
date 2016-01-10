package services.slickbacked

import models._
import services.UrlShortenerService

import play.api.Logger
import com.google.inject.Inject
import com.google.inject.Singleton

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scalaz._
import Scalaz._


@Singleton
class UrlShortenerServiceImpl @Inject() (dbProvider: DatabaseProvider) extends UrlShortenerService {

  private val db = dbProvider.db
  private val dm = dbProvider.dataModel

  import dm.driver.api._

  def get(hash: String): Future[IssueMessage \/ Option[ShortUrl]] = {
    db.run {
      {
        dm.urlHashMap.filter(_.hash === hash).result.headOption
      }.transactionally
    }.map(_.map(result => ShortUrl(result.originalUrl, result.hash)).right).recover {
      case err =>
        Logger.warn(s"There was a internal database error when reading: $err")
        "There was a internal database error when reading".id[Issue].left
    }
  }

  def put(shortUrl: ShortUrl): Future[IssueMessage \/ Long] = {
    db.run {
      {
        dm.urlHashMap returning dm.urlHashMap.map(_.id) into ((row, id) => row.copy(id = id)) +=
          dm.UrlHashMap(
            originalUrl = shortUrl.original_url,
            hash = shortUrl.hash
          )
      }.transactionally
    }.map(_.id.right).recover {
      case err =>
        Logger.warn(s"There was a internal database error when inserting: $err")
        "There was a internal database error when inserting".id[Issue].left
    }
  }

  def update(id: Long, shortUrl: ShortUrl): Future[IssueMessage \/ Unit] = {
    db.run {
      {
        dm.urlHashMap.filter(row => row.id === id).map { row =>
          (row.hash, row.originalUrl)
        }.update((shortUrl.hash, shortUrl.original_url))
      }.transactionally
    }.map(_ => ().right).recover {
      case err =>
        Logger.warn(s"There was a internal database error when updating $id: $err")
        "There was a internal database error when updating".id[Issue].left
    }
  }
}