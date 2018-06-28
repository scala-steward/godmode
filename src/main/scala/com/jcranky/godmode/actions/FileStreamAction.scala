package com.jcranky.godmode.actions

import java.nio.file.{Files, Path, Paths}

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import fs2.{io, text}

case class FileStreamAction(fileName: String) {

  val sourcePath: Path = Paths.get(s"src/main/resources/$fileName")
  val targetPath: Path = Paths.get(s"target/$fileName")

  /**
    * Process a file in a streaming fashion, passing each line to the `logic` function, and writing this function's
    * result to a new file.
    */
  def compile[F[_]](transformation: String => F[String])(implicit F: Sync[F]): F[Unit] =
    for {
      _ <- F.delay(if (Files.exists(targetPath)) Files.delete(targetPath))
      _ <- F.delay(Files.createDirectories(targetPath.getParent))
      _ <- processStream(transformation)
    } yield ()

  private def processStream[F[_]: Sync](logic: String => F[String]): F[Unit] = io.file.readAll[F](sourcePath, 4096)
    .through(text.utf8Decode)
    .through(text.lines)
    .map(_.trim)
    .evalMap(logic)
    .intersperse("\n")
    .through(text.utf8Encode)
    .through(io.file.writeAll(targetPath))
    .compile.drain

}
