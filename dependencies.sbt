
val catsEffectVersion = "1.0.0-RC2"
val fs2Version = "0.10.5"
val doobieVersion = "0.5.3"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "co.fs2" %% "fs2-core" % fs2Version,
  "co.fs2" %% "fs2-io" % fs2Version,
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "com.lihaoyi" %% "utest" % "0.6.4" % "test"
)
