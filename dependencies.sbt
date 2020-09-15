
val catsEffectVersion = "2.1.4"
val fs2Version = "2.4.4"
val doobieVersion = "0.9.2"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "co.fs2" %% "fs2-core" % fs2Version,
  "co.fs2" %% "fs2-io" % fs2Version,
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "io.monix" %% "minitest" % "2.8.2" % "test"
)
