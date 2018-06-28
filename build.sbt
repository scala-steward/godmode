name := "godmode"
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

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "utest" % "0.6.4" % "test"
)
