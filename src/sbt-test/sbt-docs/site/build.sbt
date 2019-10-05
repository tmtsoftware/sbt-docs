version in ThisBuild := "0.1-SNAPSHOT"

lazy val docs = project
  .in(file("."))
  .enablePlugins(DocsPlugin)
  .settings(
    name := "Docs Test",
    docsRepo := "https://github.com/tmtsoftware/sbt-docs",
    docsParentDir := "sbt-docs",
    gitCurrentRepo := "https://github.com/tmtsoftware/sbt-docs",
    paradoxLeadingBreadcrumbs := List("Alphabet" -> "https://abc.xyz/", "Google" -> "https://www.google.com"),
    paradoxTheme := None
  )
