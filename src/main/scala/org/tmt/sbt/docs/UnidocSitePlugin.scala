package org.tmt.sbt.docs

import sbt.Keys._
import sbt._

/**
 * Configures UnidocPlugin
 */
object UnidocSitePlugin extends AutoPlugin {
  import sbtunidoc.{BaseUnidocPlugin, JavaUnidocPlugin, ScalaUnidocPlugin}
  import BaseUnidocPlugin.autoImport.unidoc
  import JavaUnidocPlugin.autoImport._
  import ScalaUnidocPlugin.autoImport._
  import com.typesafe.sbt.site.SitePlugin.autoImport._

  override def requires: Plugins = ScalaUnidocPlugin && JavaUnidocPlugin

  def excludeJavadoc: Set[String] = Set("internal", "scaladsl")
  def excludeScaladoc: String     = Seq("akka").mkString(":")

  override def projectSettings: Seq[Setting[_]] = Seq(
    ScalaUnidoc / siteSubdirName := "/api/scala",
    addMappingsToSiteDir(ScalaUnidoc / packageDoc / mappings, ScalaUnidoc / siteSubdirName),
    JavaUnidoc / siteSubdirName := "/api/java",
    filterNotSources(JavaUnidoc / unidoc / sources, excludeJavadoc),
    JavaUnidoc / unidoc / javacOptions := {
      Seq("-Xdoclint:none", "--ignore-source-errors", "--no-module-directories")
    },
    addMappingsToSiteDir(JavaUnidoc / packageDoc / mappings, JavaUnidoc / siteSubdirName),
    ScalaUnidoc / unidoc / scalacOptions ++= Seq("-skip-packages", excludeScaladoc, "-Xfatal-warnings"),
    autoAPIMappings := true
  )

  def filterNotSources(filesKey: TaskKey[Seq[File]], subPaths: Set[String]): Setting[Task[Seq[File]]] = {
    filesKey := filesKey.value.filterNot(file => subPaths.exists(file.getAbsolutePath.contains))
  }
}
