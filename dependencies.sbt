
val catsEffectVersion = "1.4.0"
val fs2Version = "1.0.0"
val doobieVersion = "0.5.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "co.fs2" %% "fs2-core" % fs2Version,
  "co.fs2" %% "fs2-io" % fs2Version,
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "io.monix" %% "minitest" % "2.8.2" % "test"
)
