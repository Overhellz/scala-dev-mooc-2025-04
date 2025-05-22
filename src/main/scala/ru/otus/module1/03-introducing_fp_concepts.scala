package ru.otus.module1

import ru.otus.module1.opt.Cat

import scala.annotation.tailrec
import scala.language.postfixOps

/**
 * referential transparency
 */

// recursion

object recursion {

  /**
   * Реализовать метод вычисления n!
   * n! = 1 * 2 * ... n
   */

  def fact(n: Int): Int = {
    var _n = 1
    var i = 2
    while (i <= n) {
      _n *= i
      i += 1
    }
    _n
  }

  def factRec(n: Int): Int = if (n <= 0) 1 else n * factRec(n - 1)

  def factTailRec(n: Int, accum: Int): Int = {
    @tailrec
    def loop(n: Int, accum: Int): Int =
      if (n <= 0) accum
      else loop(n - 1, n * accum)

    loop(n, 1)
  }

  /**
   * реализовать вычисление N числа Фибоначчи
   * F0 = 0, F1 = 1, Fn = Fn-1 + Fn - 2
   */
  def fib(n: Int): BigInt = {
    @tailrec
    def fibTailRec(n: Int, nextToLast: BigInt, last: BigInt): BigInt = {
      if (n == 0) nextToLast
      else fibTailRec(n - 1, last, nextToLast + last)
    }

    if (n <= 0) 0
    else fibTailRec(n, 0, 1)
  }
}

object hof {

  def dumb(string: String): Unit = {
    Thread.sleep(1000)
    println(string)
  }

  // обертки
  def logRunningTime[A, B](f: A => B): A => B = a => {
    val start = System.currentTimeMillis()
    val result = f(a)
    val end = System.currentTimeMillis()
    println(s"Running time: ${end - start}")
    result
  }

  // изменение поведения ф-ции
  def isOdd(i: Int): Boolean = i % 2 > 0

  def not[A](f: A => Boolean): A => Boolean = a => !f(a)

  val isEven: Int => Boolean = not(isOdd)

  isOdd(5) // true
  isEven(5) // false

  // изменение самой функции
  def sum(x: Int, y: Int): Int = x + y

  def partial[A, B, C](a: A)(f: (A, B) => C): B => C =
    f.curried(a)
}

/**
 * Реализуем тип Option
 */
object opt {

  trait Animal

  case class Cat() extends Animal

  case class Dog() extends Animal

  /**
   *
   * Реализовать структуру данных Option, который будет указывать на присутствие либо отсутствие результата
   */

  // Invariance
  // + Covariance Если А является подтипом В, то Option[A] является подтипом Option[B]
  // - Contravariance Если А является подтипом В, то Option[A] является супер типом Option[B]

  // Function1[-R, +T]
  val f1: String => Unit = ???
  val f2: Any => Unit = ???

  def foo(f: String => Unit) = f("Hello")

  foo(f2)
  foo(f1)

  sealed trait Option[+T] {
    def isEmpty: Boolean = if (this.isInstanceOf[None.type]) true else false

    def get: T = if (this.isInstanceOf[None.type]) throw new Exception("None get")
    else {
      val r = this.asInstanceOf[Some[T]]
      r.v
    }

    def map[B](f: T => B): Option[B] = flatMap(v => Option(f(v)))

    def flatMap[B](f: T => Option[B]): Option[B] = this match {
      case Some(value) => f(value)
      case None => None
    }
  }

  case class Some[T](v: T) extends Option[T]

  case object None extends Option[Nothing]

  object Option {
    def apply[T](v: T): Option[T] =
      if (v == null) None else Some(v)
  }

  val opt1: Option[Int] = Some(42)
  val opt2: Option[Option[Int]] = opt1.map(i => Option(i + 1))
  val opt3: Option[Int] = opt1.flatMap(i => Option(i + 1))

  /**
   *
   * Реализовать метод printIfAny, который будет печатать значение, если оно есть
   */
  def printIfAny[A](option: Option[A]): Unit = {
    if (!option.isEmpty) println(option.get)
  }

  /**
   *
   * Реализовать метод zip, который будет создавать Option от пары значений из 2-х Option
   */
  def zip[A, B](first: Option[A], second: Option[B]): Option[(A, B)] = (first, second) match {
    case (Some(first), Some(second)) => Some((first, second))
    case _ => None
  }

  /**
   *
   * Реализовать метод filter, который будет возвращать не пустой Option
   * в случае если исходный Option не пуст и предикат от значения = true
   */
  def filter[A](option: Option[A], predicate: A => Boolean): Option[A] = option match {
    case Some(option) if predicate.apply(option) => Some(option)
    case _ => None
  }
}

object list {

  /**
   * Реализовать одно связанный иммутабельный список List
   * Список имеет два случая:
   * Nil - пустой список
   * Cons - непустой, содержит первый элемент (голову) и хвост (оставшийся список)
   */
  //  def treat(a: Option[Animal]) = ???

  sealed trait List[+T] {

    // prepend
    def ::[TT >: T](elem: TT): List[TT] = Cons(elem, this)
  }

  case class ::[T](elem: T, tail: List[T]) extends List[T]

  // Пустой список
  case object Nil extends List[Nothing]

  // Непустой список
  case class Cons[+T](head: T, tail: List[T]) extends List[T]

  val l1 = List(1, 2, 3)

  val l2: List[Cat] = List(Cat())

  /**
   * Конструктор, позволяющий создать список из N - го числа аргументов
   * Для этого можно воспользоваться *
   *
   * Например, вот этот метод принимает некую последовательность аргументов с типом Int и выводит их на печать
   * def printArgs(args: Int*) = args.foreach(println(_))
   */
  object List {
    def apply[A](list: A*): List[A] =
      if (list.isEmpty) Nil
      else new::(list.head, apply(list.tail: _*))
  }

  /**
   * Реализовать метод reverse, который позволит заменить порядок элементов в списке на противоположный
   */
  def reverse[T](list: List[T]): List[T] = {
    @tailrec
    def loop(list: List[T], acc: List[T]): List[T] = list match {
      case Nil => acc
      case ::(head, tail) => loop(tail, head :: acc)
    }

    loop(list, Nil)
  }

  /**
   * Реализовать метод map для списка который будет применять некую ф-цию к элементам данного списка
   */
  def map[T, R](list: List[T], f: T => R): List[R] = {
    @tailrec
    def loop(list: List[T], acc: List[R]): List[R] = list match {
      case Nil => acc
      case ::(head, tail) => loop(tail, f(head) :: acc)
    }

    reverse(loop(list, Nil))
  }

  /**
   * Реализовать метод filter для списка который будет фильтровать список по некому условию
   */
  def filter[T](list: List[T], predicate: T => Boolean): List[T] = {
    @tailrec
    def loop(list: List[T], acc: List[T]): List[T] = list match {
      case Nil => acc
      case ::(head, tail) if predicate(head) => loop(tail, head :: acc)
      case ::(_, tail) => loop(tail, acc)
    }

    reverse(loop(list, Nil))
  }

  /**
   * Написать функцию incList которая будет принимать список Int и возвращать список,
   * где каждый элемент будет увеличен на 1
   */
  def incList(list: List[Int]): List[Int] = map(list, (x: Int) => x + 1)

  /**
   * Написать функцию shoutString которая будет принимать список String и возвращать список,
   * где к каждому элементу будет добавлен префикс в виде '!'
   */
  def shoutString(list: List[String]): List[String] = map(list, (str: String) => '!' + str)
}
