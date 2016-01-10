package services

import models._

import play.api._
import play.api.mvc._
import play.api.Logger

import scala.concurrent.Future


object Global extends GlobalSettings {

  override def onHandlerNotFound(request: RequestHeader) = Future.successful {
    Issues.asNotFound("UnsupportedEndpoint".id[Issue])
  }

  override def onError(request: RequestHeader, ex: Throwable) = Future.successful {
    Issues.asInternalServerError("InternalError".id[Issue])
  }

  override def onBadRequest(request: RequestHeader, error: String) = Future.successful {
    Logger.warn(s"Bad Request made: $error")
    Issues.asBadRequest("BadRequest".id[Issue])
  }
}