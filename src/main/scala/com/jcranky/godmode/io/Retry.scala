package com.jcranky.godmode.io

import cats.effect.{Sync, Timer}
import cats.syntax.applicativeError._
import cats.syntax.apply._

import scala.concurrent.duration._

object Retry {

  implicit class RetryOps[F[_], A](fa: F[A]) {

    def retry(delay: FiniteDuration = 1.second, maxRetries: Int = 7)(implicit timer: Timer[F], F: Sync[F]): F[A] =
      fa.handleErrorWith { error =>
        if (maxRetries > 0)
          F.delay(println(s"F[A] failed with [$error], retrying")) *> timer.sleep(delay) *> fa.retry(delay * 2, maxRetries - 1)
        else
          F.raiseError(error)
      }
  }
}
