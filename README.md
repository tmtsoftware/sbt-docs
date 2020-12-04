# sbt-docs

SBT plugin for publishing markdown documentation to github pages

## Usage

This plugin requires sbt 1.0.0+

## Getting Started

### Setup
Create `project/plugins.sbt`:

```sbt
resolvers += Resolver.bintrayRepo("twtmt", "sbt-plugins")
addSbtPlugin("com.github.tmtsoftware" % "sbt-docs" % "0.2.0")
```

Inside `build.sbt`, add `DocsPlugin` to a subproject:

```sbt
lazy val docs = project
  .in(file("."))
  .enablePlugins(DocsPlugin)
  .settings(
    docsRepo := "https://github.com/tmtsoftware/sbt-docs",
    docsParentDir := "sbt-docs",
    gitCurrentRepo := "https://github.com/tmtsoftware/sbt-docs"
  )
```

- *docsRepo*: The remote git repository where documents will be published"
- *docsParentDir*: Parent directory where generated documentation will be published
- *gitCurrentRepo*: The remote git repository associated with this project

### Testing

Run `test` for regular unit tests.

Run `scripted` for [sbt script tests](http://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html).

### Publishing

#### Steps to publish for first time

1. publish your source to GitHub
2. [create a bintray account](https://bintray.com/signup/index) and [set up bintray credentials](https://github.com/sbt/sbt-bintray#publishing)
3. create a bintray repository `sbt-plugins`
4. update your bintray publishing settings in `build.sbt`
5. `sbt publish`
6. [request inclusion in sbt-plugin-releases](https://bintray.com/sbt/sbt-plugin-releases)
7. [Add your plugin to the community plugins list](https://github.com/sbt/website#attention-plugin-authors)
8. [Claim your project an Scaladex](https://github.com/scalacenter/scaladex-contrib#claim-your-project)

#### Steps to publish successive versions

1. run `sbt clean test scripted`
2. create git tag with version to publish and push tag and sources to github
2. ensure you have bintray credentials by running `bintrayWhoami` from sbt shell.
For setting up credentials, please refer [set up bintray credentials](https://github.com/sbt/sbt-bintray#publishing)
4. run `sbt publish` (This will publish to bintray)
