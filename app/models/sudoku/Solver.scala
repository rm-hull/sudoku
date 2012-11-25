package sudoku

import Grid._

trait Solver {
  def solve(grid: Grid): Grid

  def ??? = throw new Error("NYI")
}

object SimpleSolver extends Solver {

  private def knownDigits(groups: List[IndexedSeq[Choices]]): Choices =
    (for {
      cells <- groups
      cell <- cells
      if (cell.size == 1)
    } yield cell.head).toSet

  /**
   * Removes those possible values from the cell at (x,y) in the grid from
   * the known digits in the current row, column and square. If there is
   * nothing to remove, nil is returned, otherwise the set of possible
   * digits is returned.
   */
  private def singleCandidate(grid: Grid, x: Int, y: Int): Choices = {
    val a = grid.cell(x, y)
    val b = knownDigits(List(grid.row(y), grid.column(x), grid.box(x,y)))
    a diff b
  }

  private def freq(coll: Seq[Choices]): Map[Int, Int] =
    coll.reduce { _ ++ _ }
        .groupBy { x => x }
        .map { case (k,v) => (k, v.size) }
        .toMap
        .withDefaultValue(0)

  /**
   * Checks to see if any of the possible digits for cell at the given
   * position is the only candidate, but is hidden among other candidates.
   */
  private def hiddenSingle(grid: Grid, x: Int, y: Int): Choices = {
    def chk(f: => IndexedSeq[Choices], digit: Int): Boolean = //true
      freq(f)(digit) == 1

    val uniq = (for {
                  poss <- grid.cell(x, y)
                  if (chk(grid.row(y), poss) ||
                      chk(grid.column(x), poss) ||
                      chk(grid.box(x,y), poss))
                } yield poss)

    if (uniq.size == 1) uniq
    else Set.empty
  }

  private def reducer(f: (Grid, Int, Int) => Choices)(grid: Grid): Grid = {
    val cells = for {
      y <- Range.inclusive(0, 8)
      x <- Range.inclusive(0, 8)
    } yield {
      val curr = grid.cell(x, y)
      if (curr.size == 1) curr
      else {
        val poss = f(grid, x, y)
        if (poss.isEmpty) curr
        else poss
      }
    }

    grid.updated(cells.toIndexedSeq)
  }

  def compose[T](f: T => T, g: T => T): T => T = { x: T => f(g(x)) }

  def solve(grid: Grid): Grid = {
    val f = compose(reducer(singleCandidate)(_:Grid), reducer(hiddenSingle)(_:Grid))
    def iter(prev: Grid, curr: Grid): Grid = {
      if (prev.cells == curr.cells || curr.isSolved) curr
      else iter(curr, f(curr))
    }
    iter(Grid.empty, grid)
  }
}
