package ru.otus.module1

import scala.util.Random

object hw2 {

  sealed trait Ball

  case object BlackBall extends Ball

  case object WhiteBall extends Ball

  private val urn: List[Ball] = List(BlackBall, BlackBall, BlackBall, WhiteBall, WhiteBall, WhiteBall)

  def shuffleUrn(): List[Ball] = Random.shuffle(urn)

  def hasWhiteBallAtFirstOrSecond(urn: List[Ball]): Boolean = {
    urn.head == WhiteBall || urn(1) == WhiteBall
  }

  def makeExperiment(count: Int): Int = {
    val experiments: Seq[List[Ball]] = (1 to count).map(_ => shuffleUrn()).filter(hasWhiteBallAtFirstOrSecond)
    experiments.take(count).size
  }

  def main(args: Array[String]): Unit = {

    val count1000 = 1_000
    val count10000 = 10_000
    val count100000 = 100_000
    val count1000000 = 1_000_000

    val experiment1000: Int = makeExperiment(count1000)
    val experiment10000: Int = makeExperiment(count10000)
    val experiment100000: Int = makeExperiment(count100000)
    val experiment1000000: Int = makeExperiment(count1000000)

    println(s"Result: $experiment1000 from $count1000")
    println(s"Result: $experiment10000 from $count10000")
    println(s"Result: $experiment100000 from $count100000")
    println(s"Result: $experiment1000000 from $count1000000")

    //    Result: 796 from 1000
    //    Result: 7925 from 10000
    //    Result: 80034 from 100000
    //    Result: 800676 from 1000000
  }
}
