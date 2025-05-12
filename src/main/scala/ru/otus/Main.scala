package ru.otus

import ru.otus.module1.{hof, type_system}


object App {
  def main(args: Array[String]): Unit = {

    val r: String => Unit = hof.logRunningTime(hof.dumb)

    r("Hello world")

  }
}