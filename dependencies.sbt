
val catsEffectVersion = "2.4.1"
val fs2Version = "3.0.1"
val doobieVersion = "0.12.1"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "co.fs2" %% "fs2-core" % fs2Version,
  "co.fs2" %% "fs2-io" % fs2Version,
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "io.monix" %% "minitest" % "2.9.4" % "test"
)
