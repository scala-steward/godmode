package com.jcranky.godmode.actions

import cats.data.NonEmptyList
import cats.instances.string._
import cats.syntax.eq._
import utest._

object ScalyrActionTest extends TestSuite {

  val tests = Tests {
    val config = ScalyrConfig("./scalyr", "my-app-host", "my-token", ScalyrEU)
    val action = ScalyrAction(config, NonEmptyList.of("text to search", "jcranky"), "10 minutes")

    "assemble the command line parts" - {
      "log host" - {
        val filter = action.logHostFilter
        assert(filter === """ $serverHost="my-app-host" """)
      }

      "token" - {
        val token = action.token
        assert(token === """--token="my-token"""")
      }

      "scalyr host" - {
        val scalyrHost = action.scalyrHost
        assert(scalyrHost === """--server="eu.scalyr.com"""")
      }

      "start filter" - {
        val startFilter = action.startFilter
        assert(startFilter === """--start="10 minutes"""")
      }

      "search query" - {
        val query = action.query
        assert(query === """"text to search" "jcranky"""")
      }
    }

    "assemble the final scalyr command line" - {
      val line = action.scalyrLine
      val expected = """./scalyr query ' $serverHost="my-app-host"  "text to search" "jcranky"' --token="my-token" --server="eu.scalyr.com" --start="10 minutes" --output json | jq .matches[].message"""

      assert(line === expected)
    }
  }
}
