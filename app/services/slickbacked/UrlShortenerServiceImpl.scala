package services.slickbacked

import models._
import services.UrlShortenerService

import com.google.inject.Inject
import com.google.inject.Singleton

import scalaz._


@Singleton
class UrlShortenerServiceImpl @Inject() (dbProvider: DatabaseProvider) extends UrlShortenerService {

  private val db = dbProvider.db
  private val dm = dbProvider.dataModel

  def get(hash: String): EitherIssueShortUrl = {
    ???
  }

  def put(shortUrl: ShortUrl): EitherIssueShortUrl = {
    ???
  }
}