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
   */
  def box2linear(box: String): String = {
    require(box.size == 81, "Box size was " + box.size + " [" + box + "]")
    val partitions = box.sliding(27,27).toList
    val rows = for {
      i <- 0 to 2
      j <- 0 to 2
    } yield partitions(j)
              .sliding(3,3)
              .drop(i)
              .sliding(1,3)
              .flatten
              .reduce(_ + _)
    rows.mkString
  }

  /**
   * Reforms a linear representation of a grid, into a box.
   * It is true that:
   *
   *      linear2box(box2linear(x)) == x
   *
   * Implementation-wise this method is repeated application of box2linear.
   */
  def linear2box(linear: String): String = {
    require(linear.size == 81, "Linear size was " + linear.size + " [" + linear + "]")
    box2linear(box2linear(linear))
  }
}
