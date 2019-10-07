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
    siteSubdirName in ScalaUnidoc := "/api/scala",
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc),
    siteSubdirName in JavaUnidoc := "/api/java",
    filterNotSources(sources in (JavaUnidoc, unidoc), excludeJavadoc),
    addMappingsToSiteDir(mappings in (JavaUnidoc, packageDoc), siteSubdirName in JavaUnidoc),
    scalacOptions in (ScalaUnidoc, unidoc) ++= Seq("-skip-packages", excludeScaladoc, "-Xfatal-warnings"),
    autoAPIMappings := true
  )

  def filterNotSources(filesKey: TaskKey[Seq[File]], subPaths: Set[String]): Setting[Task[Seq[File]]] = {
    filesKey := filesKey.value.filterNot(file => subPaths.exists(file.getAbsolutePath.contains))
  }
}
