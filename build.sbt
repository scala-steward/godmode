
name := "godmode"
organization := "com.jcranky"
scalaVersion := "2.13.1"

version := "0.1-M8"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:higherKinds",
  "-Ywarn-unused",
  "-Xfatal-warnings",
  "-Xlint",
  "-Xmacro-settings:materialize-derivations"
)

testFrameworks += new TestFramework("minitest.runner.Framework")

cancelable in Global := true

licenses += ("GPL-3.0", url("http://www.opensource.org/licenses/gpl-3.0.html"))

bintrayReleaseOnPublish in ThisBuild := false

bintrayPackageLabels := Seq("cats", "cats-effect", "io", "godmode")
