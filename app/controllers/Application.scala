package controllers

import scala.io._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.concurrent._
import play.api.Play.current

import sudoku._
import sudoku.Converter._

object Application extends Controller {

  def index = Action {
    grid((templateGrids.size * math.random).toInt)
  }

  def grid(idx: Int) = Action {
    val grid = templateGrids.lift(idx).getOrElse(Grid.empty)

    Ok(
      views.html.index(
        Converter.transform(
          grid.flatten(" ").mkString)))
  }

  def solve(cells: String) = Action {
    val grid = Grid("TODO", transform(cells))
    if (grid.isLegal) Async {
      Akka.future { CombinationSolver.solve(grid) }.map { result =>
        Ok(
          Json.toJson(
            Map("status" -> "OK",
                "solved" -> result.isSolved.toString,
                "grid"   -> transform(result.flatten(" ")))))
      }
    }
    else BadRequest(
      Json.toJson(
        Map("status"  -> "Error",
            "message" -> "Invalid grid data")))
  }

  lazy val templateGrids: IndexedSeq[Grid] =
    Source.fromFile("app/assets/resources/sudoku.txt")
          .getLines
          .grouped(10)
          .map(data => Grid(data))
          .toIndexedSeq
}
