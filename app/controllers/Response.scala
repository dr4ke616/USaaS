package controllers

import models._
import models.JsonOps._

import play.api.mvc.Result
import play.api.libs.json.JsResult

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scalaz._
import Scalaz._

object Response {

  def fromJsResult[T](invalidResult: IssueMessage => Result)(result: JsResult[T]) = EitherT[Future, Result, T] {
    Future.successful {
      result.asEither.leftMap {
        errors => invalidResult(errors.toString.id[models.Issue])
      }.disjunction
    }
  }

  def fromEitherIssue[T](issueResult: IssueMessage => Result)(either: IssueMessage \/ T): EitherT[Future, Result, T] = {
    EitherT[Future, Result, T] {
      Future.successful(either.leftMap(issueResult))
    }
  }

  def fromFutureEitherIssue[T](issueResult: IssueMessage => Result)(fut: Future[IssueMessage \/ T]): EitherT[Future, Result, T] = {
    EitherT[Future, Result, T] {
      fut.map(_.leftMap(issueResult))
    }
  }

  def fromFutureEitherIssueOrOption[T](issueResult: IssueMessage => Result, emptyResult: Result)(fut: Future[IssueMessage \/ Option[T]]): EitherT[Future, Result, T] = {
    EitherT[Future, Result, T] {
      fut.map(_.leftMap(issueResult).flatMap(_ \/> emptyResult))
    }
  }

}