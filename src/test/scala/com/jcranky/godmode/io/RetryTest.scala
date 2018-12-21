package com.jcranky.godmode.io

import cats.effect.{IO, Timer}
import com.jcranky.godmode.io.Retry._
import minitest.SimpleTestSuite

import scala.concurrent.ExecutionContext.Implicits.global

object RetryTest extends SimpleTestSuite {

  // TODO: replace with a custom testAsyncIO ?
  testAsync("retry a failed io") {
    implicit val timer: Timer[IO] =
      IO.timer(scala.concurrent.ExecutionContext.Implicits.global)

    var fail = true
    val io = IO {
      if (fail) {
        fail = false
        throw new RuntimeException("failed")
      }
      else "success"
    }

    val futureResult = io.retry().unsafeToFuture()
    futureResult.map { result =>
      assertEquals[String](result, "success")
    }
  }
}
