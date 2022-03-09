package org.tmt.sbt.docs

import _root_.io.github.jonas.paradox.material.theme.ParadoxMaterialThemePlugin
import _root_.io.github.jonas.paradox.material.theme.ParadoxMaterialThemePlugin.autoImport._
import com.lightbend.paradox.sbt.ParadoxPlugin.autoImport._
import com.typesafe.sbt.site.paradox.ParadoxSitePlugin
import org.tmt.sbt.docs.DocKeys._
import sbt.Keys.{baseDirectory, scalaBinaryVersion, sourceDirectory, version}
import sbt._

/**
 * Enables paradox documentation and material theme also configures custom javadoc and scaladoc properties in paradoxSettings
 */
object ParadoxMaterialSitePlugin extends AutoPlugin {

  override def requires: Plugins = ParadoxSitePlugin && ParadoxMaterialThemePlugin

  override def projectSettings: Seq[Setting[_]] =
    ParadoxMaterialThemePlugin.paradoxMaterialThemeSettings(Compile) ++
      Seq(
        Compile / paradox / sourceDirectory                := baseDirectory.value / "src" / "main",
        Compile / paradox / paradoxTheme / sourceDirectory := (Compile / paradox / sourceDirectory).value / "_template",
        Compile / paradoxMaterialTheme := {
          ParadoxMaterialTheme()
            .withFavicon("assets/tmt_favicon.ico")
            .withCustomStylesheet("assets/docs.css")
            .withRepository(new URI(gitCurrentRepo.value))
        },
        Compile / paradoxProperties ++= Map(
          "version"             -> version.value,
          "scala.binaryVersion" -> scalaBinaryVersion.value,
          "scaladoc.base_url"   -> s"https://tmtsoftware.github.io/${docsParentDir.value}/${version.value}/api/scala",
          "javadoc.base_url"    -> s"https://tmtsoftware.github.io/${docsParentDir.value}/${version.value}/api/java",
          "github.dir.base_url" -> githubBaseUrl(gitCurrentRepo.value, version.value, "tree"),
          "github.base_url"     -> githubBaseUrl(gitCurrentRepo.value, version.value, "blob"),
          "esw_backend_template.base_url" -> githubBaseUrl(
            "https://github.com/tmtsoftware/esw-backend-template.g8",
            readVersion("ESW_BACKEND_TEMPLATE_VERSION"),
            "blob"
          ),
          "esw_ui_template.base_url" -> githubBaseUrl(
            "https://github.com/tmtsoftware/esw-ui-template.g8",
            readVersion("ESW_UI_TEMPLATE_VERSION"),
            "blob"
          ),
          "csw_template.base_url" -> githubBaseUrl(
            "https://github.com/tmtsoftware/csw.g8",
            readVersion("CSW_TEMPLATE_VERSION"),
            "blob"
          ),
          "extref.esw_ts.base_url"       -> s"https://tmtsoftware.github.io/esw-ts/${readVersion("ESW_TS_VERSION")}/%s",
          "extref.csw.base_url"          -> s"https://tmtsoftware.github.io/csw/${readVersion("CSW_VERSION")}/%s",
          "extref.esw.base_url"          -> s"https://tmtsoftware.github.io/esw/${readVersion("ESW_VERSION")}/%s",
          "extref.csw_scaladoc.base_url" -> s"https://tmtsoftware.github.io/csw/${readVersion("CSW_VERSION")}/api/scala/%s",
          "extref.csw_javadoc.base_url"  -> s"https://tmtsoftware.github.io/csw/${readVersion("CSW_VERSION")}/api/java/%s"
        )
      )

  // export <DEPENDENT_REPO>_VERSION env variable which is compatible with RELEASED REPO
  // For example, if you are releasing ESW, export CSW_VERSION which is compatible with release of ESW
  private def readVersion(envVersionKey: String): String =
    (sys.env ++ sys.props).get(envVersionKey) match {
      case Some(v) => v
      case None    => "0.1.0-SNAPSHOT"
    }

  private def githubBaseUrl(repo: String, version: String, baseForGithub: String) = {
    println(s"version picked by sbt-docs: repo: ${repo} version: ${version} base: ${baseForGithub}")
    val baseRepoUrl =
      s"$repo/$baseForGithub" // baseForGithub will be tree for a github directory and blob for a github file to avoid 301 redirect error on link validation
    if (version.endsWith("SNAPSHOT")) s"$baseRepoUrl/master"
    else s"$baseRepoUrl/v$version"
  }
}
