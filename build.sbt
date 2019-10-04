name := """sbt-docs"""
organization := "com.github.tmtsoftware"
version := "0.1-SNAPSHOT"

sbtPlugin := true

// ScalaTest
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

bintrayPackageLabels := Seq("sbt","plugin")
bintrayVcsUrl := Some("""git@github.com:org.tmt/sbt-docs.git""")

initialCommands in console := """import org.tmt.sbt.docs._"""

enablePlugins(ScriptedPlugin)
// set up 'scripted; sbt plugin for testing sbt plugins
scriptedLaunchOpts ++=
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)

addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.2")
addSbtPlugin("io.github.jonas" % "sbt-paradox-material-theme" % "0.6.0")