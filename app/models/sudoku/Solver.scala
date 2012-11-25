import sudoku.Grid._
import sudoku.Grid
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
  private def validDigits(grid: Grid, x: Int, y: Int): Choices = {
    val a = grid.cell(x, y)
    val b = knownDigits(List(grid.row(y), grid.column(x), grid.box(x,y)))
    a diff b
  }

  /**
   * Reduces the elements in the grid where any cells which have only one
   * candidate can safely be assigned that value.
   */
  private def singleCandidateReduction(grid: Grid): Grid = {
    val cells = for {
      x <- Range.inclusive(0, 8)
      y <- Range.inclusive(0, 8)
    } yield {
      val curr = grid.cell(x, y)
      if (curr.size == 1) curr
      else {
        val poss = validDigits(grid, x, y)
        if (poss.isEmpty) curr
        else poss
      }
    }

    grid.updated(cells)
  }

  def solve(grid: Grid): Grid = {
    val f = singleCandidateReduction _ //compose hiddenSingleReduction
    def iter(prev: Grid, curr: Grid): Grid = {
      if (prev.cells == curr.cells || curr.isSolved) curr
      else iter(curr, f(curr))
    }
    iter(Grid.empty, grid)
  }
}
