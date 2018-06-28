
name := "godmode"
organization := "com.jcranky.godmode"
scalaVersion := "2.12.6"

version := "0.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:higherKinds",
  "-Yno-adapted-args",
  "-Ywarn-unused",
  "-Xfatal-warnings",
  "-Xlint",
  "-Xmacro-settings:materialize-derivations"
)

testFrameworks += new TestFramework("com.jcranky.godmode.GodModeTestFramework")

cancelable in Global := true

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

bintrayReleaseOnPublish in ThisBuild := false

licenses += ("GPL-3.0", url("http://www.opensource.org/licenses/gpl-3.0.html"))

bintrayPackageLabels := Seq("cats", "cats-effect", "io", "godmode")
