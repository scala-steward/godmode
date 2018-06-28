package com.jcranky.godmode.io

import cats.effect.{Sync, Timer}
import cats.syntax.applicativeError._
import cats.syntax.apply._

import scala.concurrent.duration._

object Retry {

  def retry[F[_], A](ioa: F[A], initialDelay: FiniteDuration = 1.second, maxRetries: Int = 7)(implicit timer: Timer[F], F: Sync[F]): F[A] =
    ioa.handleErrorWith { error =>
      if (maxRetries > 0)
        timer.sleep(initialDelay) *> retry(ioa, initialDelay * 2, maxRetries - 1)
      else
        F.raiseError(error)
    }
}
