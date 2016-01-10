package controllers

import Response._
import services._

import models._
import models.JsonOps._

import com.google.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.mvc._
import play.api.libs.json.Json

import scalaz._
import Scalaz._


class Shortener @Inject() (urlShortenerService: UrlShortenerService, hashService: HashService) extends Controller {


  def get(id: Long) = Action.async { implicit request =>

    val response = for {
      body              <- urlShortenerService.get(id)                      |> fromFutureEitherIssueOrOption(Issues.asInternalServerError, NotFound)
    } yield Ok(Json.toJson(body))

    response.merge
  }

  def put() = Action.async(parse.tolerantJson) { implicit request =>

    val response = for {
      requestBody       <- request.body.validate[ShortUrl]                    |> fromJsResult(Issues.asBadRequest)
      urlId             <- urlShortenerService.put(requestBody)               |> fromFutureEitherIssue(Issues.asInternalServerError)
      generatedHash     <- hashService.generate(urlId)                        |> fromEitherIssue(Issues.asInternalServerError)
      responseBody      = requestBody.copy(id = Option(urlId), hash = Option(generatedHash))
      _                 <- urlShortenerService.update(urlId, responseBody)    |> fromFutureEitherIssue(Issues.asInternalServerError)
    } yield Created(Json.toJson(responseBody))

    response.merge
  }
}