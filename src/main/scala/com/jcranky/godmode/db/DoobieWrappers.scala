package com.jcranky.godmode.db

import cats.effect.Async
import cats.syntax.flatMap._
import doobie.implicits._
import doobie.util.composite.Composite
import doobie.util.fragment.Fragment
import doobie.util.log.LogHandler
import doobie.util.transactor.Transactor

trait DoobieWrappers {

  def query[F[_], T: Composite](fragment: Fragment, logQueries: Boolean = false)(xa: Transactor[F])(implicit F: Async[F]): F[List[T]] = {
    implicit val logHandler: LogHandler = getLogger(logQueries)
    val q = fragment.query.to[List]
    val transaction: F[List[T]] = q.transact(xa)

    F.delay(println(s"[DoobieWrappers.query] $fragment")).flatMap(_ => transaction)
  }

  def update[F[_]](fragment: Fragment, logQueries: Boolean = false)(xa: Transactor[F])(implicit F: Async[F]): F[Int] = {
    implicit val logHandler: LogHandler = getLogger(logQueries)
    val transaction = fragment.update.run.transact(xa)

    F.delay(println(s"[DoobieWrappers.update] $fragment")).flatMap(_ => transaction)
  }

  private def getLogger(logQueries: Boolean): LogHandler =
    if (logQueries) LogHandler.jdkLogHandler else LogHandler.nop
}

object DoobieWrappers extends DoobieWrappers
