name := """sbt-docs"""
organization := "com.github.tmtsoftware"
version := "0.1.0-SNAPSHOT"
description := "An sbt plugin for publishing markdown documentation to github pages"
homepage := scmInfo.value map (_.browseUrl)
scmInfo := Some(ScmInfo(url("https://github.com/tmtsoftware/sbt-docs"), "scm:git:git@github.com:tmtsoftware/sbt-docs.git"))
scalaVersion := "2.12.12"
sbtPlugin := true
publishMavenStyle := true

// ScalaTest
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"

bintrayPackageLabels := Seq("sbt", "plugin")
bintrayVcsUrl := Some("""git@github.com:org.tmt/sbt-docs.git""")

initialCommands in console := """import org.tmt.sbt.docs._"""

enablePlugins(ScriptedPlugin)
// set up 'scripted; sbt plugin for testing sbt plugins
scriptedLaunchOpts ++=
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)

addSbtPlugin("com.typesafe.sbt"      % "sbt-ghpages"                % "0.6.3")
addSbtPlugin("com.typesafe.sbt"      % "sbt-site"                   % "1.4.0")
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox"                % "0.8.0")
addSbtPlugin("io.bullet"             % "sbt-paradox-material-theme" % "0.7.0")
addSbtPlugin("com.eed3si9n"          % "sbt-unidoc"                 % "0.4.3")

libraryDependencies += "com.sun.activation" % "javax.activation" % "1.2.0"
resolvers += "Jenkins repo" at "https://repo.jenkins-ci.org/public/"
addSbtPlugin("ohnosequences" % "sbt-github-release" % "0.7.0")
