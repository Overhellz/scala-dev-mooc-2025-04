package ru.otus.module1

import java.util.concurrent.{ExecutorService, Executors}
import scala.collection.mutable

object concurrency {
  class Thread1 extends Thread{
    override def run(): Unit = {
      println(s"Hello from ${Thread.currentThread().getName}")
    }
  }

  def printRunningTime[T](v: => Unit): Unit = {
    val start = System.currentTimeMillis()
    v
    val end = System.currentTimeMillis()
    println(s"Running time: ${end - start}")
  }

  def getRatesLocation1: ToyFuture[Int] = ToyFuture{
    Thread.sleep(1000)
    println("GetRatesLocation1")
    10
  }(executors.executor1)

  def getRatesLocation2: ToyFuture[Int] = ToyFuture{
    Thread.sleep(2000)
    println("GetRatesLocation2")
    20
  }(executors.executor1)

  def async(f: => Unit): Unit = {
    val t = new Thread{
      override def run(): Unit = f
    }
    t.start()
    t.join()
  }

  def async2[A](f: => A): A = {
    var r: A = null.asInstanceOf[A]
    val t = new Thread{
      override def run(): Unit = r = f
    }
    t.start()
    r
  }

  class ToyFuture[T] private (v: () => T, executor: ExecutorService) {
    private var isCompleted: Boolean = false
    private var r: T = null.asInstanceOf[T]
    private val q = mutable.Queue[T => _]()

    def onComplete[U](f: T => U): Unit = {
      if(isCompleted) f(r)
      else q.enqueue(f)
    }

    def flatMap[B](f: T => ToyFuture[B]): ToyFuture[B] = ???
    def map[B](f: T => B): ToyFuture[B] = ???

    private def start() = {
      val t = new Runnable{
        override def run(): Unit = {
          r = v()
          isCompleted = true
          while (q.nonEmpty){
            q.dequeue()(r)
          }
        }
      }
      executor.execute(t)
    }
  }

  object ToyFuture {
    def apply[T](f: => T)(executors: ExecutorService): ToyFuture[T] = {
      val future = new ToyFuture[T](() => f, executors)
      future.start()
      future
    }
  }
}

object executors {

  val executor1 = Executors.newFixedThreadPool(2)
  val executor2 = Executors.newSingleThreadExecutor()
  val executor3 = Executors.newCachedThreadPool()
  val executor4 = Executors.newWorkStealingPool(4)
}

