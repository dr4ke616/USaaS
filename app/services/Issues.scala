package services

import models._
import models.JsonOps._

import play.api.mvc.Results._
import play.api.http.Status._
import play.api.libs.json.{Json, JsPath}
import play.api.data.validation.ValidationError


object Issues {

  def asBadRequest(msg: IssueMessage) = BadRequest(Json.toJson(Seq(Issue(BAD_REQUEST, msg))))
  def asBadRequest(msg: Seq[IssueMessage]) = BadRequest(Json.toJson(msg.map(m => Issue(BAD_REQUEST, m))))
  def asBadRequest(e: Seq[(JsPath, Seq[ValidationError])], title: String = "BadRequest") = BadRequest(Json.toJson(convert(BAD_REQUEST, title, e)))

  def asNotFound(msg: IssueMessage) = NotFound(Json.toJson(Seq(Issue(NOT_FOUND, msg))))
  def asNotFound(msg: Seq[IssueMessage]) = NotFound(Json.toJson(msg.map(m => Issue(NOT_FOUND, m))))
  def asNotFound(e: Seq[(JsPath, Seq[ValidationError])], title: String = "NotFound") = NotFound(Json.toJson(convert(NOT_FOUND, title, e)))

  def asInternalServerError(msg: IssueMessage) = InternalServerError(Json.toJson(Seq(Issue(INTERNAL_SERVER_ERROR, msg))))
  def asInternalServerError(msg: Seq[IssueMessage]) = InternalServerError(Json.toJson(msg.map(m => Issue(INTERNAL_SERVER_ERROR, m))))
  def asInternalServerError(e: Seq[(JsPath, Seq[ValidationError])], title: String = "InternalServerError") = InternalServerError(Json.toJson(convert(INTERNAL_SERVER_ERROR, title, e)))

  private def convert(code: Int, title: String, errors: Seq[(JsPath, Seq[ValidationError])]): Seq[Issue] = {
    errors.flatMap {
      case (path, verrors) => verrors.map(convert(code, path, _))
    }
  }

  private def convert(code: Int, path: JsPath, verror: ValidationError): Issue = {
    Issue(code, s"${path.toString}: ${verror.message}".id[Issue])
  }
}
