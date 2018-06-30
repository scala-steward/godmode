package com.jcranky.godmode.actions

import cats.effect.Sync
import cats.syntax.apply._
import cats.syntax.flatMap._

import scala.collection.mutable.ListBuffer
import scala.sys.process.{Process, ProcessLogger}

case class ShellAction(cmd: String, verbose: Boolean = false) {

  /**
    * Just return the command that would be executed when running this action.
    */
  def dryRun[F[_] : Sync]: F[String] =
    Sync[F].pure(cmd)

  def compile[F[_]](implicit F: Sync[F]): F[String] = {

    // this is necessary so that command piping and commands like "cut -d ' ' -f5" can be parsed and work correctly
    def toBash(cmd: String): Seq[String] =
      Seq("bash", "-c", cmd)

    // TODO: if we use ProcessIO instead, can we interact with the shell?
    val outs = ListBuffer.empty[String]
    val errs = ListBuffer.empty[String]

    val logger = ProcessLogger(str => outs.append(str), str => errs.append(str))

    val verbosity = if (verbose) F.delay {
      println("x" * 50 + " Outs:")
      println(outs.mkString("[", ",", "]"))
      println("x" * 50 + " Errs:")
      println(errs.mkString("[", ",", "]"))
      println("x" * 50)
    } else F.unit

    val delayedProcess = F.delay {
      val p = Process(toBash(cmd)).run(logger)
      p.exitValue()
    } <* verbosity

    delayedProcess.flatMap { exit =>
      if (exit != 0)
        F.raiseError(new RuntimeException(s"shell action failed:\n${errs.mkString("\n")}"))
      else
      // TODO: bleh, stuff goes to err as well !!! what is the 'right' way to deal with this? return both maybe?
      // TODO: also, for some reason not everything seems to be being sent to outs, when the output too long :(
        F.pure(outs.mkString("\n"))
    }
  }
}
