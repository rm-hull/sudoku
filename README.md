sudoku
======

A Scala / Play 2.0 sudoku solver for deployment on Heroku at http://sudoku.destructuring-bind.org. 
Initially written to compare and contrast an identically implemented Clojure
solver, whose source can be found here: 
https://github.com/rm-hull/project-euler/blob/master/src/euler096.clj,
but mainly to play with Play.

    HOW TO PLAY: Fill in the grid so that every row, every column and every 3 × 3
    box contains the digits 1–9. There’s no maths involved. You solve the puzzle with
    reasoning and logic.

_Advice on how to play Sudoku, The Independent Newspaper_

How to build & deploy
=====================

* install sbt 0.11.3  if you do not have it already. You can get it from
  here: https://github.com/harrah/xsbt/wiki/Getting-Started-Setup
 
* execute `sbt` and then `help play` for play specific commands

* execute `sbt` and then `compile` to build the project

* execute `sbt` and then `run` to run the built-in development server

* once the development server is running, documentation is available
  at http://localhost:9000/@documentation

* to run directly from the shell, execute `sbt` and then `stage`; this will
  create a `target/start` script which allows the application to run stand-alone

Trivia
======

There are 6,670,903,752,021,072,936,960 ways of filling in a blank Sudoku 
grid, according to http://www.afjarvis.staff.shef.ac.uk/sudoku/sudoku.pdf

49151 grids dynamically sourced from http://school.maths.uwa.edu.au/~gordon/sudokumin.php,
randomly displayed whenever the page is reloaded, alternatively, go to 
http://sudoku.destructuring-bind.org/grid/XXXX to load a specific grid.

http://sudoku.destructuring-bind.org/grid/52 is reputed to be the hardest 
Sudoku in the world [http://www.telegraph.co.uk/science/science-news/9359579/Worlds-hardest-sudoku-can-you-crack-it.html]
