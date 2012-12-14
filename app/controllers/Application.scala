package controllers

import scala.io._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.concurrent._
import play.api.Play.current

import Execution.Implicits.defaultContext

import sudoku._
import sudoku.Converter._

object Application extends Controller {

  def index = grid((templateData.size * math.random).toInt)

  def grid(idx: Int) = Action { request =>
    val grid = templateData.lift(idx)
                           .map(x => Grid("Grid:" + idx, x))
                           .getOrElse(Grid.empty)

    Ok(views.html.index(transform(grid.flatten(" ").mkString)))
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

  def localData(fname: String): IndexedSeq[String] =
    Source.fromFile(fname)
          .getLines
          .grouped(10)
          .map(data => data.tail.mkString)
          .toIndexedSeq

  def remoteData(url: String): IndexedSeq[String] =
    Source.fromURL(url)
          .getLines
          .toIndexedSeq

  lazy val templateData: IndexedSeq[String] =
    localData("app/assets/resources/sudoku.txt") ++
    remoteData("http://school.maths.uwa.edu.au/~gordon/sudoku17")
}
