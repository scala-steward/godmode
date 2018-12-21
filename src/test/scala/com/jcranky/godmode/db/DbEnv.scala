package com.jcranky.godmode.db

import java.sql.DriverManager

import cats.effect.{ContextShift, IO}
import doobie.util.transactor.Transactor

import scala.concurrent.ExecutionContext
import scala.io.Source
import scala.util.Random

case class DbConfig(host: String, port: Int, schemaName: String, dbName: String, user: String, password: String)

case class DbEnv(dbConfig: DbConfig) {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    s"jdbc:postgresql://${dbConfig.host}:${dbConfig.port}/${dbConfig.dbName}?currentSchema=${dbConfig.schemaName}",
    dbConfig.user,
    dbConfig.password
  )
}

object DbEnv {

  def setupDb(): DbEnv = {
    val schemaName = createSchemaName()

    // those have to match the values in the docker-compose.yaml file
    val dbConfig = DbConfig(host = "localhost", port = 6666, schemaName = schemaName, dbName = "godmode-db", user = "godmode", password = "godmode-pass")

    exec(dbConfig)(s"CREATE SCHEMA $schemaName;")

    exec(dbConfig)(
      s"""
         |SET search_path = $schemaName, public;
         |${Source.fromFile("src/test/resources/db/test-db-setup.sql").getLines().mkString("\n")}
       """.stripMargin)

    DbEnv(dbConfig)
  }

  def tearDownDb(dbEnv: DbEnv): Unit =
    exec(dbEnv.dbConfig)(s"DROP SCHEMA ${dbEnv.dbConfig.schemaName} CASCADE;")

  // the first char cannot be a digit
  private def createSchemaName(): String =
    "godmode_" + Random.alphanumeric.take(20).mkString

  private def exec(dbConfig: DbConfig)(sql: String): Unit = {
    val jdbcUrl = s"jdbc:postgresql://${dbConfig.host}:${dbConfig.port}/${dbConfig.dbName}"

    closing(DriverManager.getConnection(jdbcUrl, dbConfig.user, dbConfig.password)) { conn =>
      closing(conn.createStatement()) { stmt =>
        stmt.execute(sql)
      }
    }
  }

  def closing[A <: AutoCloseable, B](a: A)(fn: A => B): B =
    try fn(a) finally a.close()
}
