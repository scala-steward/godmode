package com.jcranky.godmode.io

import cats.effect.{Sync, Timer}
import cats.syntax.applicativeError._
import cats.syntax.apply._

import scala.concurrent.duration._

object Retry {

  implicit class RetryOps[F[_], A](fa: F[A]) {

    def retry(initialDelay: FiniteDuration = 1.second, maxRetries: Int = 7)(implicit timer: Timer[F], F: Sync[F]): F[A] =
      fa.handleErrorWith { error =>
        if (maxRetries > 0)
          timer.sleep(initialDelay) *> fa.retry(initialDelay * 2, maxRetries - 1)
        else
          F.raiseError(error)
      }
  }
}
