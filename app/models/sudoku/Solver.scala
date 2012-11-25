package sudoku

import Grid._

trait Solver {
  def solve(grid: Grid): Grid

  def ??? = throw new Error("NYI")
}

/**
 * Iteratively reduces down the named grid until either a solution is
 * found (and the solution is returned) or successive reductions produce
 * no improvements.
 */
object SimpleSolver extends Solver {

  private def knownDigits(cells: IndexedSeq[Choices]): Choices =
    (for {
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
    val b = knownDigits(grid.row(y) ++  grid.column(x) ++  grid.box(x, y))
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
    def chk(f: => IndexedSeq[Choices], digit: Int): Boolean = freq(f)(digit) == 1

    val uniq = (for {
                  poss <- grid.cell(x, y)
                  if (chk(grid.row(y), poss) ||
                      chk(grid.column(x), poss) ||
                      chk(grid.box(x, y), poss))
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

    grid.updated(cells)
  }

  private def compose[T](f: T => T, g: T => T) = { x: T => f(g(x)) }

  def solve(grid: Grid): Grid = {
    val f = compose(reducer(singleCandidate)(_:Grid), reducer(hiddenSingle)(_:Grid))
    def iter(prev: Grid, curr: Grid): Grid = {
      if (prev.cells == curr.cells || curr.isSolved) curr
      else iter(curr, f(curr))
    }
    iter(Grid.empty, grid)
  }
}

/**
 * Performs a depth-first search of all the possible grids, returned as a
 * lazy sequence. It is up to the consumer of the sequence to terminate
 * iterating over the sequence on receipt of a completed solution. Note
 * that this is different to the simple solver implementation.
 */
object WhatIfSolver extends Solver {

  /**
   * Returns a map comprised of the first cell which has a choice of
   * possible values, along with the offset in the grid.
   */
  private def firstChoice(grid: Grid): Option[(Choices, Int)] =
    grid.cells.zipWithIndex.find { case(choice, idx) => choice.size > 1 }

  def nextGenerations(grid: Grid): Set[Grid] = firstChoice(grid) match {
    case None => Set.empty
    case Some((choices, idx)) => for {
      poss <- choices
    } yield SimpleSolver.solve(grid.updated(idx, Set(poss)))
  }

  def from(initial: Stream[Grid]): Stream[Grid] = initial match {
    case Stream() => Stream.empty
    case x #:: xs => x #:: from(nextGenerations(x).toStream append xs)
  }

  def solve(grid: Grid): Grid =
    from(Stream(grid)).find(x => x.isSolved).getOrElse(grid)
}

object CombinationSolver extends Solver {

  def solve(grid: Grid): Grid = {
    val firstPass = SimpleSolver.solve(grid)
    if (grid.isSolved) firstPass
    else WhatIfSolver.solve(firstPass)
  }
}
