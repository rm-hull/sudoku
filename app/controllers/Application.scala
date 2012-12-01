package controllers

import scala.io._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import sudoku._

object Application extends Controller {

  def index = Action {
    val rnd = (templateGrids.size * math.random).toInt
    val grid = templateGrids(rnd)

    Ok(
      views.html.index(
        Converter.transform(
          grid.flatten(" ").mkString)))
  }

  def solve(name: String, grid: String) = Action {
    val result = CombinationSolver.solve(
                   Grid(
                     name,
                     Converter.transform(grid)))
    Ok(
      Json.toJson(
        Map("status" -> "OK",
            "solved" -> result.isSolved.toString,
            "grid"   -> Converter.transform(result.flatten(" ")))))
  }

  lazy val templateGrids: IndexedSeq[Grid] =
    Source.fromFile("app/assets/resources/sudoku.txt")
          .getLines
          .grouped(10)
          .map(data => Grid(data))
          .toIndexedSeq
}
