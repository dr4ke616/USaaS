package services

import models.ShortUrl
import mixin.ProvidedDatabase
import org.scalatest.time.{Span, Seconds}
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

  override implicit val patienceConfig = PatienceConfig(timeout = Span(1, Seconds))

  val urlShortenerService = new UrlShortenerServiceImpl(databaseProvider)
  val hash = "some_hash"

  "UrlShortenerService" should "create a new entry and read it back" in {

    val shortUrl = ShortUrl(original_url = "http://some/url", hash = Option(hash))
    val \/-(returnedId) = urlShortenerService.put(shortUrl).futureValue
    returnedId should === (1.toLong)

    val \/-(retrieved) = urlShortenerService.get(returnedId).futureValue
    retrieved should === (Option(shortUrl.copy(id = Option(returnedId))))
  }

  it should "create then be able to update an entry" in {

    // Create
    val shortUrl = ShortUrl(original_url = "http://some/url", hash = None)
    val \/-(returnedId) = urlShortenerService.put(shortUrl).futureValue
    returnedId should === (1.toLong)

    // Update
    urlShortenerService.update(returnedId, shortUrl.copy(hash = Option(hash))).futureValue
    val \/-(updated) = urlShortenerService.get(returnedId).futureValue
    updated should === (Option(shortUrl.copy(id = Option(returnedId), hash = Option(hash))))
  }
}
