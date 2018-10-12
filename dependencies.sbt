
val catsEffectVersion = "1.0.0"
val fs2Version = "1.0.0"
val doobieVersion = "0.5.3"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "co.fs2" %% "fs2-core" % fs2Version,
  "co.fs2" %% "fs2-io" % fs2Version,
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "com.lihaoyi" %% "utest" % "0.6.6" % "test"
)
