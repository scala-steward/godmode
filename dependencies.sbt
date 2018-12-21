
val catsEffectVersion = "1.0.0"
val fs2Version = "1.0.2"
val doobieVersion = "0.5.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "co.fs2" %% "fs2-core" % fs2Version,
  "co.fs2" %% "fs2-io" % fs2Version,
  "org.tpolecat" %% "doobie-core" % doobieVersion
) ++ testLibraries


// test-scoped libraries
val minitestVersion = "2.2.2"

val testLibraries = Seq(
  "io.monix" %% "minitest" % minitestVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion
).map(_ % Test)
