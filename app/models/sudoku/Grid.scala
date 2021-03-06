package sudoku

import scala.io.Source._
import Grid._

object Grid {

  type Choices = Set[Int]

  val empty = Grid("Empty", "0" * 81)

  def apply(filename: String): Grid =
    apply(fromFile(filename).getLines.toList)

  def apply(data: Seq[String]): Grid = {
    data match {
      case name :: cells => apply(name, cells.mkString)
      case _ => throw new IllegalArgumentException("Unexpected data format")
    }
  }

  def apply(name: String, cells: String): Grid = {
    require(cells.length == 81, "Invalid number of cells: " + cells.length)
    new Grid(name, cells.map(createChoices).toIndexedSeq)
  }

  private def createChoices(c: Char): Choices = {
    val i = c.toInt - 48
    if (i == 0 || !c.isDigit) 1 to 9 toSet
    else Set(i)
  }

  private def offset(x: Int, y: Int): Int = x + (y * 9)

  private def isComplete(cells: Seq[Choices]): Boolean = {
    val merged = cells.flatten.distinct
    merged.sum == 45 && merged.product == 362880
  }

  private def isValid(cells: Seq[Choices]): Boolean =
    cells.filter(cell => cell.size == 1).flatten.groupBy(x => x).forall { case (k, v) => v.size == 1 }
}

class Grid(val name: String, val cells: IndexedSeq[Choices], val iteration: Int = 0) {

  def cell(offset: Int): Choices = cells(offset)

  def cell(x: Int, y: Int): Choices = cell(offset(x, y))

  def slice(offset: Int, size: Int): IndexedSeq[Choices] =
    cells slice(offset, offset + size)

  def row(y: Int): IndexedSeq[Choices] = slice(offset(0, y), 9)

  def column(x: Int): IndexedSeq[Choices] =
    cells drop(x) sliding(1, 9) map(_(0)) toIndexedSeq

  def box(x: Int, y: Int): IndexedSeq[Choices] = {
    def norm(a: Int): Int = 3 * (a / 3)
    0 to 2 map(i => slice(offset(norm(x), norm(y) + i), 3)) reduce(_ ++ _)
  }

  def box(offset: Int): IndexedSeq[Choices] =
    box(3 * (offset / 3), 3 * (offset % 3))

  lazy val isSolved: Boolean = {
    cells.forall(cell => cell.size == 1) &&
    (0 to 8).forall { i =>
      isComplete(row(i)) &&
      isComplete(column(i)) &&
      isComplete(box(i))
    }
  }

  lazy val isLegal: Boolean = {
    (0 to 8).forall { i =>
      isValid(row(i)) &&
      isValid(column(i)) &&
      isValid(box(i))
    }
  }

  override def toString =
    name +
    ", legal: " + isLegal +
    ", solved: " + isSolved +
    ", iteration: " + iteration +
    ", cells: " + cells

  def updated(x: Int, y: Int, newChoices: Choices): Grid =
    updated(offset(x, y), newChoices)

  def updated(offset: Int, newChoices: Choices): Grid =
    updated(cells.updated(offset, newChoices))

  def updated(newCells: IndexedSeq[Choices]): Grid =
    new Grid(name, newCells, iteration + 1)

  def flatten(unsolved: String = "0"): String =
    cells.map(x => if (x.size == 1) x.head else unsolved).mkString
}

