package org.tmt.sbt.docs

import com.typesafe.sbt.site.SitePlugin.autoImport._
import org.tmt.sbt.docs.DocKeys._
import sbt.Keys._
import sbt._
import sbtunidoc.BaseUnidocPlugin.autoImport._
import sbtunidoc.JavaUnidocPlugin.autoImport.JavaUnidoc
import sbtunidoc.ScalaUnidocPlugin.autoImport.ScalaUnidoc

object Settings {

  def makeSiteMappings(project: Project = null): Seq[Def.Setting[_]] = Def.settings {
    Seq(
      mappings in makeSite := {
        val Version = version.value
        val siteMappings =
          Def.taskDyn {
            val default = (mappings in makeSite).value
            if (project == null) Def.task(default)
            else Def.task(default ++ (mappings in makeSite in project).value)
          }.value

        // copy all artifacts inside `parentDir` directory
        val siteMappingsWithoutVersion = siteMappings.map {
          case (file, output) => (file, s"/${docsParentDir.value}/" + output)
        }
        val siteMappingsWithVersion = siteMappings.map {
          case (file, output) => (file, s"/${docsParentDir.value}/" + Version + output)
        }

        // keep documentation for SNAPSHOT versions in SNAPSHOT directory. (Don't copy SNAPSHOT docs to top level)
        // If not SNAPSHOT version, then copy latest version of documentation to top level as well as inside corresponding version directory
        if (Version.endsWith("-SNAPSHOT")) siteMappingsWithVersion
        else siteMappingsWithoutVersion ++ siteMappingsWithVersion
      }
    )
  }

  def docExclusions(projects: Seq[ProjectReference]): Seq[Setting[_]] =
    projects.map(p => sources in (Compile, doc) in p := Seq.empty) ++ Seq(
      unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(projects: _*),
      unidocProjectFilter in (JavaUnidoc, unidoc) := inAnyProject -- inProjects(projects: _*)
    )

}
