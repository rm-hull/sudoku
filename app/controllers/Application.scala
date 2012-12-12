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

  def localGrids(fname: String): IndexedSeq[Grid] =
    Source.fromFile(fname)
          .getLines
          .grouped(10)
          .map(data => Grid(data))
          .toIndexedSeq

  def remoteGrids(url: String): IndexedSeq[Grid] =
    Source.fromURL(url)
          .getLines
          .zipWithIndex
          .map { case(data, index) => Grid("Grid:" + index, data) }
          .toIndexedSeq

  lazy val templateGrids: IndexedSeq[Grid] =
    localGrids("app/assets/resources/sudoku.txt") ++
    remoteGrids("http://school.maths.uwa.edu.au/~gordon/sudoku17")
}
