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

    val experiment1000: Double = makeExperiment(count1000)
    val experiment10000: Double = makeExperiment(count10000)
    val experiment100000: Double = makeExperiment(count100000)
    val experiment1000000: Double = makeExperiment(count1000000)

    val result1000: Double = experiment1000 / count1000
    val result10000: Double = experiment10000 / count10000
    val result100000: Double = experiment100000 / count100000
    val result1000000: Double = experiment1000000 / count1000000

    println(result1000)
    println(result10000)
    println(result100000)
    println(result1000000)

    //    0.788
    //    0.8029
    //    0.79968
    //    0.799049
  }
}
