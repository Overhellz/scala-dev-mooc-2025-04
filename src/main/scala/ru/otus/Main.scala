package ru.otus

import ru.otus.module1.{hof, type_system}


object App {
  def main(args: Array[String]): Unit = {
    trait A extends Serializable
    object A
  }
}