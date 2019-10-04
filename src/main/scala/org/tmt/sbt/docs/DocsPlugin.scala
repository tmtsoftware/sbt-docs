package org.tmt.sbt.docs

import _root_.io.github.jonas.paradox.material.theme.ParadoxMaterialThemePlugin
import _root_.io.github.jonas.paradox.material.theme.ParadoxMaterialThemePlugin.autoImport._
import com.lightbend.paradox.sbt.ParadoxPlugin.autoImport._
import com.typesafe.sbt.SbtGit.GitKeys
import com.typesafe.sbt.sbtghpages.GhpagesPlugin
import com.typesafe.sbt.sbtghpages.GhpagesPlugin.autoImport._
import com.typesafe.sbt.site.SitePlugin.autoImport._
import com.typesafe.sbt.site.paradox.ParadoxSitePlugin
import com.typesafe.sbt.site.paradox.ParadoxSitePlugin.autoImport._
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object DocsPlugin extends AutoPlugin {

  override def trigger = noTrigger

  override def requires: Plugins = JvmPlugin && GhpagesPlugin && ParadoxSitePlugin && ParadoxMaterialThemePlugin

  object autoImport {
    val docsRepo       = settingKey[String]("The remote git repository where documents will be published")
    val docsParentDir  = settingKey[String]("Parent directory where generated documentation will be published")
    val gitCurrentRepo = settingKey[String]("The remote git repository associated with this project")
  }

  import autoImport._

  override def projectSettings: Seq[Setting[_]] =
    ParadoxMaterialThemePlugin.paradoxMaterialThemeSettings(Paradox) ++ Seq(
      ghpagesBranch := "master",
      includeFilter in ghpagesCleanSite := { pathname: File =>
        pathname.getAbsolutePath.contains(s"${docsParentDir.value}/${version.value}")
      },
      GitKeys.gitRemoteRepo := docsRepo.value,
      sourceDirectory in Paradox := baseDirectory.value / "src" / "main",
      sourceDirectory in (Paradox, paradoxTheme) := (sourceDirectory in Paradox).value / "_template",
      paradoxMaterialTheme in Paradox := {
        val repo = gitCurrentRepo.value
        (paradoxMaterialTheme in Paradox).value
          .withFavicon("assets/tmt_favicon.ico")
          .withRepository(new URI(repo))
      },
      paradoxProperties in Paradox ++= Map(
        "version"             → version.value,
        "scala.binaryVersion" → scalaBinaryVersion.value,
        "github.base_url"     → githubBaseUrl(gitCurrentRepo.value, version.value),
        "extref.csw.base_url" → s"https://tmtsoftware.github.io/csw/${readVersion("CSW_VERSION")}/%s",
        "extref.esw.base_url" → s"https://tmtsoftware.github.io/esw/${readVersion("ESW_VERSION")}/%s"
      ),
      (mappings in makeSite) := {
        val Version      = version.value
        val siteMappings = (mappings in makeSite).value

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

  // export CSW_VERSION env variable which is compatible with csw
  private def readVersion(envVersionKey: String): String =
    (sys.env ++ sys.props).get(envVersionKey) match {
      case Some(v) => v
      case None    => "0.1-SNAPSHOT"
    }

  private def githubBaseUrl(repo: String, version: String) = {
    val baseRepoUrl = s"$repo/tree"
    if (version == "0.1-SNAPSHOT") s"$baseRepoUrl/master"
    else s"$baseRepoUrl/v$version"
  }

}
