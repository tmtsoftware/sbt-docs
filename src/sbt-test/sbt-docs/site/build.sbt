import org.tmt.sbt.docs.Settings
import org.tmt.sbt.docs.DocKeys._

name := "Docs Test"
version := "0.1.0-SNAPSHOT"
docsRepo in ThisBuild := "https://github.com/tmtsoftware/sbt-docs"
docsParentDir in ThisBuild := "sbt-docs"
gitCurrentRepo in ThisBuild := "https://github.com/tmtsoftware/sbt-docs"

lazy val site = project
  .in(file("."))
  .aggregate(api, docs)
  .enablePlugins(UnidocSitePlugin, GithubPublishPlugin)
  .settings(Settings.makeSiteMappings(docs))

lazy val api = project.enablePlugins(UnidocSitePlugin)

lazy val docs = project
  .enablePlugins(ParadoxMaterialSitePlugin)
  .settings(
    paradoxRoots := List("a.html"),
    paradoxLeadingBreadcrumbs := List("Alphabet" -> "https://abc.xyz/", "Google" -> "https://www.google.com"),
    paradoxTheme := None
  )
