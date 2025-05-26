package ru.otus.module1

import scala.annotation.tailrec
import scala.util.Random

object hw2 {

  sealed trait Ball

  object BlackBall extends Ball

  object WhiteBall extends Ball

  val balls: List[Ball] = List(BlackBall, BlackBall, BlackBall, WhiteBall, WhiteBall, WhiteBall)

  def getFirstIndex(size: Int): Int = Random.between(0, size)

  @tailrec
  def getSecondIndex(size: Int, firstIndex: Int): Int = {
    val index = Random.between(0, size)

    if (index == firstIndex) getSecondIndex(size, firstIndex)
    else index
  }

  def oneExperiment(balls: List[Ball]): Boolean = {
    val first = getFirstIndex(balls.size)
    val second = getSecondIndex(balls.size, first)

    (balls(first), balls(second)) match {
      case (WhiteBall, _) => true
      case (BlackBall, WhiteBall) => true
      case _ => false
    }
  }

  def main(args: Array[String]): Unit = {
    val experiments: Seq[BallsExperiment] = (1 to count).map(_ => new BallsExperiment())




    println(oneExperiment(balls))
    println(oneExperiment(balls))
    println(oneExperiment(balls))
    println(oneExperiment(balls))
  }
}
