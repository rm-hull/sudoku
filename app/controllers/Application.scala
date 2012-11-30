package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._

import sudoku._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def solve(name: String, grid: String) = Action {
    val result = SimpleSolver.solve(
                   Grid(
                     name,
                     Converter.box2linear(grid)))
    Ok(
      Json.toJson(
        Map("status" -> "OK",
            "solved" -> result.isSolved.toString,
            "grid" -> (if (result.isSolved) Converter.linear2box(result.cells.flatten.mkString) else ""))))
  }
}
