package com.jcranky.godmode.db

import java.util.UUID

import cats.effect.IO
import doobie.implicits._
import doobie.postgres.implicits._
import minitest.TestSuite

import scala.concurrent.ExecutionContext.Implicits.global

// TODO: replace testAsync with a custom testAsyncIO ?
object DoobieWrappersTest extends TestSuite[DbEnv] {

  override def setup(): DbEnv =
    DbEnv.setupDb()

  override def tearDown(dbEnv: DbEnv): Unit =
    DbEnv.tearDownDb(dbEnv)

  testAsync(".query should return all entries from a DB table") { dbEnv =>
    val expected = List(
      "super mario odyssey",
      "the legend of zelda: breath of the wild"
    )

    val gamesIO = DoobieWrappers.query[IO, Game](sql"SELECT * FROM games ORDER BY name")(dbEnv.xa).map(_.map(_.name))
    gamesIO.unsafeToFuture().map(games => assertEquals(games, expected))
  }

  testAsync(".update should update the databasae") { dbEnv =>
    val expected = List(
      "breath of the wild",
      "super mario odyssey"
    )

    val gamesIO = for {
      _ <- DoobieWrappers.update(sql"UPDATE games SET name = 'breath of the wild' WHERE name = 'the legend of zelda: breath of the wild'")(dbEnv.xa)
      games <- DoobieWrappers.query[IO, Game](sql"SELECT * FROM games ORDER BY name")(dbEnv.xa)
    } yield games.map(_.name)

    gamesIO.unsafeToFuture().map(games => assertEquals(games, expected))
  }

  testAsync(".update should insert the new row in the database") { dbEnv =>
    val newGame = Game(UUID.randomUUID(), "super mario kart 8")
    val expected = newGame.name :: List(
      "super mario odyssey",
      "the legend of zelda: breath of the wild"
    )

    val gamesIO = for {
      _ <- DoobieWrappers.update(sql"INSERT INTO games (id, name) VALUES (${newGame.uuid}, ${newGame.name})")(dbEnv.xa)
      games <- DoobieWrappers.query[IO, Game](sql"SELECT * FROM games ORDER BY name")(dbEnv.xa)
    } yield games.map(_.name)

    gamesIO.unsafeToFuture().map(games => assertEquals(games, expected))
  }
}
