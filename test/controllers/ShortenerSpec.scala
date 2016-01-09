package controllers

import models._
import models.JsonOps._

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, BeforeAndAfterEach, FlatSpec}

import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play.OneAppPerSuite

import play.api.test.Helpers._
import play.api.libs.json.Json
import play.api.test.FakeRequest


class ShortenerSpec extends FlatSpec
  with BeforeAndAfterEach
  with ScalaFutures
  with Matchers
  with MockFactory
  with OneAppPerSuite {

  private def validateShortUrl(s1: ShortUrl, s2: ShortUrl) = {
    s1.original_url should === (s2.original_url)
    s1.hash should === (s2.hash)
  }

  private def validateIssue(i1: Issue, i2: Issue) = {
    i1.code should === (i2.code)
    i1.message should === (i2.message)
  }

  val controller = new Shortener()
  val hash = "some_hash"
  val shortUrl = ShortUrl(original_url = "http://some/url", Option(hash))
  val issue = Issue(INTERNAL_SERVER_ERROR, "Internal server error".id[Issue])

  "Shortner controller" should "return 200 and ShortUrl model when requesting a successful get" in {
    val response = controller.get(hash).apply(FakeRequest())
    status(response) should === (OK)
    validateShortUrl(Json.parse(contentAsJson(response).toString).as[ShortUrl], shortUrl)
  }

  it should "return 201 with the upserted body when successfully putting a short url" in {
    val response = controller.put(hash).apply(FakeRequest())
    status(response) should === (CREATED)
    validateShortUrl(Json.parse(contentAsJson(response).toString).as[ShortUrl], shortUrl)
  }

  it should "return 500 with the issue body if an internal error should happen when getting" in {
    val response = controller.put(hash).apply(FakeRequest())
    status(response) should === (INTERNAL_SERVER_ERROR)
    validateIssue(Json.parse(contentAsJson(response).toString).as[Issue], issue)
  }

  it should "return 500 with the issue body if an internal error should happen when putting" in {
    val response = controller.put(hash).apply(FakeRequest())
    status(response) should === (INTERNAL_SERVER_ERROR)
    validateIssue(Json.parse(contentAsJson(response).toString).as[Issue], issue)
  }
}
