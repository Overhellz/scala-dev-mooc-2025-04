package ru.otus

import ru.otus.module1.{hof, pattern_matching, type_system}


object App {
  def main(args: Array[String]): Unit = {
    val arr = List(10)

    println(arr.isInstanceOf[List[String]])
  }
}