Sudoku
======

A Scala / Play! 2.x sudoku solver for deployment on Heroku at http://sudoku.destructuring-bind.org. 
Initially written to compare and contrast an identically implemented Clojure
solver, whose source can be found here: 
https://github.com/rm-hull/project-euler/blob/master/src/euler096.clj,
but mainly to play with Play!.

>"HOW TO PLAY: Fill in the grid so that every row, every column and every 3 × 3
box contains the digits 1–9. There’s no maths involved. You solve the puzzle with
reasoning and logic."

_Advice on how to play Sudoku, The Independent Newspaper_

How to build & deploy
---------------------
* install sbt 0.12.1  if you do not have it already. You can get it from
  here: http://www.scala-sbt.org/
 
* execute `sbt` and then `help play` for play specific commands

* execute `sbt` and then `compile` to build the project

* execute `sbt` and then `run` to run the built-in development server

* to run directly from the shell, execute `sbt` and then `stage`; this will
  create a `target/start` script which allows the application to run stand-alone

Trivia
------
There are 6,670,903,752,021,072,936,960 ways of filling in a blank Sudoku 
grid, according to http://www.afjarvis.staff.shef.ac.uk/sudoku/sudoku.pdf

49151 grids dynamically sourced from http://school.maths.uwa.edu.au/~gordon/sudokumin.php,
randomly displayed whenever the page is reloaded, alternatively, go to 
http://sudoku.destructuring-bind.org/grid/XXXX to load a specific grid.

http://sudoku.destructuring-bind.org/grid/52 is reputed to be the hardest 
Sudoku in the world [http://www.telegraph.co.uk/science/science-news/9359579/Worlds-hardest-sudoku-can-you-crack-it.html]

Implementation Details
----------------------
There are initially two simple solvers employed iteratively in composition 
with each other until the grid is solved or there is no further reduction
available:

* _Single cell reduction_, where cells are progressively reduced down to be 
  the only candidate in a cell.

* _Hidden single reduction_, where a candidate that appears with others in a 
  cell, but only once in a given row, column or box.

It turns out that these simple techniques will _generally_ reduce down the 
search space considerably, and in a good proportion of grids, solves the
grid entirely.

If however this repeated simple reduction still leaves cells with multiple
candidates, then a lazy stream-based depth-first-search is used (again applying
the above reductions at each step), where the first grid which 'solves' is
returned.

A side-effect of this interweaving of DFS and simple reduction considerably 
shortens the search space, and allows completely empty grids to be solved.

License
-------
Copyright (c) Richard Hull 2012

Same as the eclipse public license - v 1.0: https://www.eclipse.org/legal/epl-v10.html
