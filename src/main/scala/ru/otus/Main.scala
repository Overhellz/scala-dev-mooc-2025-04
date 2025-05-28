package ru.otus

import ru.otus.module1.concurrency
import ru.otus.module1.concurrency.{getRatesLocation1, getRatesLocation2, printRunningTime}


object App {
  def main(args: Array[String]): Unit = {

    println(s"Hello from ${Thread.currentThread().getName}")
    val t0 = new Thread{
      override def run(): Unit = {
        Thread.sleep(1000)
        println(s"Hello from ${Thread.currentThread().getName}")
      }
    }
//    val t1 = new concurrency.Thread1
//    t0.start()
//    t0.join()
//    t1.start()

    def rates = {
      getRatesLocation1.onComplete{ r1 =>
        getRatesLocation2.onComplete{ r2 =>
          println(r1 + r2)
        }
      }
    }


    printRunningTime(rates)

  }
}