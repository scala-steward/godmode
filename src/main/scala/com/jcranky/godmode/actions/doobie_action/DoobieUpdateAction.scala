package com.jcranky.godmode.actions.doobie_action

import cats.effect.{Async, Sync}
import cats.syntax.apply._
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.log.LogHandler
import doobie.util.transactor.Transactor
import cats.syntax.functor._

case class DoobieUpdateAction(fragment: Fragment, logQueries: Boolean = false) {

  def dryRun[F[_]: Sync]: F[Int] = {
    val q = fragment.update.sql

    Sync[F].delay(println(s"[DoobieUpdateAction] $q")).as(0)
  }

  def compile[F[_]](xa: Transactor[F])(implicit F: Async[F]): F[Int] = {
    implicit val logHandler: LogHandler =
      if (logQueries) LogHandler.jdkLogHandler else LogHandler.nop

    val transaction = fragment.update.run.transact(xa)

    F.delay(println(s"[DoobieUpdateAction] $fragment")) *> transaction
  }
}
