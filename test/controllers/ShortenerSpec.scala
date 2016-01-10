package controllers

import models._
import services._
import models.JsonOps._

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, BeforeAndAfterEach, FlatSpec}

import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play.OneAppPerSuite

import play.api.libs.json._
import play.api.mvc.Request
import play.api.test.Helpers._
import play.api.test.FakeRequest

import scala.concurrent.Future

import scalaz._
import Scalaz._


class ShortenerSpec extends FlatSpec
  with BeforeAndAfterEach
  with ScalaFutures
  with Matchers
  with MockFactory
  with OneAppPerSuite {

  private def validateShortUrl(s1: ShortUrl, s2: ShortUrl) = {
    s1.id should === (s2.id)
    s1.original_url should === (s2.original_url)
    s1.hash should === (s2.hash)
  }

  val hash = "some_hash"
  val shortUrl = ShortUrl(Option(1.toLong), original_url = "http://some/url", Option(hash))
  val issue = Issue(INTERNAL_SERVER_ERROR, "Internal server error".id[Issue])

  val urlShortenerService = stub[UrlShortenerService]
  val hashService = stub[HashService]

  val controller = new Shortener(urlShortenerService, hashService)

  private def setupHashService() = {
    (hashService.generate _)
      .when(*)
      .returning(hash.right[IssueMessage])
  }

  private def setupUrlShortenerService(su: Option[ShortUrl]) = {
    (urlShortenerService.get _)
      .when(*)
      .returning(Future.successful {
        su.right
      })

    (urlShortenerService.put _)
      .when(*)
      .returning(Future.successful {
        1.toLong.right
      })

    (urlShortenerService.update _)
      .when(1.toLong, *)
      .returning(Future.successful {
        ().right
      })
  }

  "Shortner controller" should "return 200 and ShortUrl model when requesting a successful get" in {
    setupUrlShortenerService(Option(shortUrl))

    val response = controller.get(1.toLong).apply(FakeRequest())
    status(response) should === (OK)
    validateShortUrl(Json.parse(contentAsJson(response).toString).as[ShortUrl], shortUrl)
  }

  it should "return 404 if nothing found" in {
    setupUrlShortenerService(None)

    val response = controller.get(2.toLong).apply(FakeRequest())
    status(response) should === (NOT_FOUND)
  }

  it should "return 401 if bad request made" in {

    val putRequest = Request(FakeRequest(), Json.parse("""{"param": "something_wrong"}"""))
    val response = controller.put().apply(putRequest)
    status(response) should === (BAD_REQUEST)
  }

  it should "return 201 with the upserted body when successfully putting a short url" in {
    setupHashService()
    setupUrlShortenerService(Option(shortUrl))

    val putRequest = Request(FakeRequest(), Json.toJson(shortUrl))
    val response = controller.put().apply(putRequest)
    status(response) should === (CREATED)
    validateShortUrl(Json.parse(contentAsJson(response).toString).as[ShortUrl], shortUrl)
  }
}
