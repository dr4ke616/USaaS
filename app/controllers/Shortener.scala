package controllers

import play.api.mvc._


class Shortener extends Controller {

  def get(hash: String) = Action { implicit request =>
    ???
  }

  def put(hash: String) = Action { implicit request =>
    ???
  }
}