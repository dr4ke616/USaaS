package controllers

import Response._
import services._

import play.api.mvc._
import com.google.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

import scalaz._
import Scalaz._


class Redirect @Inject() (urlShortenerService: UrlShortenerService, hashService: HashService) extends Controller {

  def get(hash: String) = Action.async { implicit request =>

    val response = for {
      urlId                 <- hashService.reverse(hash)          |> fromEitherIssue(Issues.asInternalServerError)
      shortUrl              <- urlShortenerService.get(urlId)     |> fromFutureEitherIssueOrOption(Issues.asInternalServerError, NotFound)
    } yield Redirect(shortUrl.original_url)

    response.merge
  }
}