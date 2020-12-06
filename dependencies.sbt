
val catsEffectVersion = "2.3.0"
val fs2Version = "2.4.6"
val doobieVersion = "0.9.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "co.fs2" %% "fs2-core" % fs2Version,
  "co.fs2" %% "fs2-io" % fs2Version,
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "io.monix" %% "minitest" % "2.9.1" % "test"
)
