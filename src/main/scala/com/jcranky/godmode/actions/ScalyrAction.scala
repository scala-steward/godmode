package com.jcranky.godmode.actions

import cats.data.NonEmptyList
import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._

// TODO: this assumes `jq` installed, probably not a good idea
/**
  * Naive scalyr action wrapping scalyr's cli. A better approach would be to use a proper JVM scalyr client, but using
  * the cli is simpler, for now.
  */
case class ScalyrAction(config: ScalyrConfig, queryTerms: NonEmptyList[String], start: String) {

  // TODO: maybe download and copy locally the scalyr cli automatically from their github?

  val scalyrBaseCmd = s"${config.scalyrHome} query"
  val logHostFilter = s""" $$serverHost="${config.host}" """
  val token = s"""--token="${config.token}""""
  val scalyrHost = s"""--server="${config.scalyrServer.domain}""""
  val startFilter = s"""--start="$start""""
  val query: String = queryTerms.map(q => s""""$q"""").toList.mkString(" ")

  // full assembled scalyr command line to be executed
  val scalyrLine: String =
    s"""$scalyrBaseCmd '$logHostFilter $query' $token $scalyrHost $startFilter --output json | jq .matches[].message"""

  def dryRun[F[_] : Sync]: F[List[String]] =
    Sync[F].pure(List(scalyrLine))

  def compile[F[_]](implicit F: Sync[F]): F[List[String]] =
    for {
      rawLog <- ShellAction(scalyrLine).compile[F]
      lines <- splitLines(rawLog)
      cleaned <- cleanLog(lines)
    } yield cleaned

  def splitLines[F[_]](rawLog: String)(implicit F: Sync[F]): F[List[String]] = F.delay {
    rawLog.trim.split("\n").toList
  }

  // TODO: a bit of brute force `replace`s to make the output more readable, need a better way to do this
  def cleanLog[F[_]](logsLines: List[String])(implicit F: Sync[F]): F[List[String]] = F.delay {
    logsLines.map(
      _.replace(raw"\n", "")
        .replace(raw"\\", "")
        .replace("\\\"", "\"")
        .replace("\"{\"log\":\"", "")
        .replace("\"}\"", "")
        .replaceAll("u003c", "<")
        .replaceAll("u003e", ">")
    )
  }
}

case class ScalyrConfig(scalyrHome: String, host: String, token: String, scalyrServer: ScalyrServer)

sealed trait ScalyrServer {
  val domain: String
}

case object ScalyrEU extends ScalyrServer {
  val domain = "eu.scalyr.com"
}

case object ScalyrCom extends ScalyrServer {
  val domain = "scalyr.com"
}
