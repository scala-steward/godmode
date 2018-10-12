package com.jcranky.godmode.io

import cats.effect.{IO, Timer}
import cats.instances.string._
import cats.syntax.eq._
import com.jcranky.godmode.io.Retry._
import utest._

object RetryTest extends TestSuite {

  val tests = Tests {
    "retry a failed io" - {
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

      assert(io.retry().unsafeRunSync() === "success")
    }
  }
}
