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

    val shortUrl = ShortUrl(original_url = "http://some/url", Option(hash))
    val \/-(id) = urlShortenerService.put(shortUrl).futureValue
    id should === (1.toLong)

    val \/-(retrieved) = urlShortenerService.get(hash).futureValue
    retrieved should === (Option(shortUrl))
  }

  it should "create then be able to update an entry" in {

    // Create
    val shortUrl = ShortUrl(original_url = "http://some/url", None)
    val \/-(id) = urlShortenerService.put(shortUrl).futureValue
    id should === (1.toLong)

    // Update
    urlShortenerService.update(id, shortUrl.copy(hash = Option(hash))).futureValue
    val \/-(updated) = urlShortenerService.get(hash).futureValue
    updated should === (Option(shortUrl.copy(hash = Option(hash))))
  }
}
