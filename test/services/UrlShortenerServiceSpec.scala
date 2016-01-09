package services

import models.ShortUrl
import mixin.ProvidedDatabase
import services.slickbacked.UrlShortenerServiceImpl

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, BeforeAndAfterEach, FlatSpec}
import org.scalatestplus.play.OneAppPerSuite

import scalaz.\/-


class UrlShortenerServiceSpec extends FlatSpec
  with ProvidedDatabase
  with ScalaFutures
  with Matchers
  with BeforeAndAfterEach
  with OneAppPerSuite {

  override def beforeEach() = cleanSchema()

  val urlShortenerService = new UrlShortenerServiceImpl(databaseProvider)
  val hash = "some_hash"

  "UrlShortenerService" should "create a new entry and read it back" in {
    val shortUrl = ShortUrl(original_url = "http://some/url", Option(hash))
    urlShortenerService.put(shortUrl).futureValue

    val \/-(retrieved) = urlShortenerService.get(hash).futureValue
    retrieved should === (shortUrl)
  }
}
