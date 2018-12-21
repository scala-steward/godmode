
name := "godmode"
organization := "com.jcranky"
scalaVersion := "2.12.8"

version := "0.1-M9"

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

cancelable in Global := true


licenses += ("GPL-3.0", url("http://www.opensource.org/licenses/gpl-3.0.html"))

bintrayReleaseOnPublish in ThisBuild := false

bintrayPackageLabels := Seq("cats", "cats-effect", "io", "godmode")
