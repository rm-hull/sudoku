sudoku
======

A Scala / Play 2.0 sudoku solver for deployment on Heroku. Initially 
written to compare and contrast an identically implemented Clojure solver, 
whose source can be found here: 
https://github.com/rm-hull/project-euler/blob/master/src/euler096.clj,
but mainly to play with Play.


How to build & deploy
=====================

* install sbt 0.11.3  if you do not have it already. You can get it from here: https://github.com/harrah/xsbt/wiki/Getting-Started-Setup

* execute `sbt` and then `help play` for play specific commands

* execute `sbt` and then `compile` to build the project

* execute `sbt` and then 'run' to run the built-in development server

* once the development server is running, documentation is available at http://localhost:9000/@documentation

* to run directly from the shell, execute `sbt` and then `stage`; this will create a `target/start` script which allows the application to run stand-alone

