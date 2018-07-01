package com.jcranky.godmode.io

import cats.effect.IO
import cats.instances.string._
import cats.syntax.eq._
import com.jcranky.godmode.io.Retry._
import utest._

import scala.concurrent.ExecutionContext.Implicits.global

object RetryTest extends TestSuite {

  val tests = Tests {
    "retry a failed io" - {
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
