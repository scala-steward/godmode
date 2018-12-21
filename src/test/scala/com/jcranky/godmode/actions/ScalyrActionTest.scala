package com.jcranky.godmode.actions

import cats.data.NonEmptyList
import cats.effect.IO
import minitest.SimpleTestSuite

import scala.concurrent.ExecutionContext.Implicits.global

object ScalyrActionTest extends SimpleTestSuite {

  val config = ScalyrConfig("./scalyr", "my-app-host", "my-token", ScalyrEU)
  val action = ScalyrAction(config, NonEmptyList.of("text to search", "jcranky"), "10 minutes")

  test("command line parts - log host") {
    assertEquals[String](action.logHostFilter, """ $serverHost="my-app-host" """)
  }

  test("command line parts - token") {
    assertEquals[String](action.token, """--token="my-token"""")
  }

  test("command line parts - scalyr host") {
    assertEquals[String](action.scalyrHost, """--server="eu.scalyr.com"""")
  }

  test("command line parts - start filter") {
    assertEquals[String](action.startFilter, """--start="10 minutes"""")
  }

  test("command line parts - search query") {
    assertEquals[String](action.query, """"text to search" "jcranky"""")
  }

  test("assemble the final scalyr command line") {
    val expected = """./scalyr query ' $serverHost="my-app-host"  "text to search" "jcranky"' --token="my-token" --server="eu.scalyr.com" --start="10 minutes" --output json | jq .matches[].message"""

    assertEquals[String](action.scalyrLine, expected)
  }

  // TODO: replace with a custom testAsyncIO ?
  testAsync("clean the output log data - replace encoded < and > with the actual characters") {
    val logLine = List("u003cTAGu003e")
    val futureResult = action.cleanLog[IO](logLine).unsafeToFuture()

    futureResult.map { result =>
      assertEquals[List[String]](result, List("<TAG>"))
    }
  }

  // TODO: replace with a custom testAsyncIO ?
  testAsync("return multiple lines in a List of Strings") {
    val rawLog =
      """
        |several lines here
        |and here
      """.stripMargin
    val futureResult = action.splitLines[IO](rawLog).unsafeToFuture()

    futureResult.map { result =>
      assertEquals[List[String]](result, List("several lines here", "and here"))
    }
  }
}
