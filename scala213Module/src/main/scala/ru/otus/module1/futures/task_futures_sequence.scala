package ru.otus.module1.futures

import scala.concurrent.{ExecutionContext, Future}

object task_futures_sequence {

  /**
   * В данном задании Вам предлагается реализовать функцию fullSequence,
   * похожую на Future.sequence, но в отличии от нее,
   * возвращающую все успешные и не успешные результаты.
   * Возвращаемое тип функции - кортеж из двух списков,
   * в левом хранятся результаты успешных выполнений,
   * в правом - результаты неуспешных выполнений.
   * Не допускается использование методов объекта Await и мутабельных переменных var
   */

  /**
   * @param futures список асинхронных задач
   * @return асинхронную задачу с кортежом из двух списков
   */
  def fullSequence[A](futures: List[Future[A]])(implicit ex: ExecutionContext): Future[(List[A], List[Throwable])] = {

    val futuresWithEither = futures.map { future =>
      future
        .map(a => Right(a): Either[Throwable, A])
        .recover { case t => Left(t): Either[Throwable, A] }
    }

    Future.sequence(futuresWithEither).map { eithers =>
      val (failures, successes) = eithers.partition(_.isLeft)
      (
        successes.collect { case Right(value) => value },
        failures.collect { case Left(error) => error }
      )
    }
  }
}
