package sudoku

/**
 * Converter which allows Sudoku grids represented as boxes (as per
 * nested HTML table/tr/td elements) can be converted to a grid with
 * linear rows.
 */
object Converter {

  /**
   * Takes a string of digits, assumed to be grouped in boxes, as follows,
   * organised alphabetically:
   *
   *     ABC JKL STU
   *     DEF MNO VWX
   *     GHI PQR YZA
   *
   *     BCD KLM TUV
   *     EFG NOP WXY
   *     HIJ QRS ZAB
   *
   *     CDE LMN UVW
   *     FGH OPQ XYZ
   *     IJK RST ABC
   *
   * And slices them so that they are represented in rows, so if passed
   * a string "ABCDEFGHIJKLMNOPQRSTUVWXYZ...", would return a string of
   * the same length but as: "ABCJKLSTUDEFMNOVWXGHIPQRYZA..."
   *
   * Note this operation is fully commutative, in as much as it is
   * possible that if rows are presented, they will be transformed into
   * boxes.
   */
  def transform(cells: String): String = {
    require(cells.size == 81, "cells size was " + cells.size + " [" + cells + "]")
    val partitions = cells.grouped(27).toIndexedSeq
    val rows = for {
      j <- 0 to 2
      i <- 0 to 2
    } yield partitions(j)
              .grouped(3)
              .drop(i)
              .sliding(1,3)
              .flatten
              .reduce(_ + _)
    rows.mkString
  }
}
