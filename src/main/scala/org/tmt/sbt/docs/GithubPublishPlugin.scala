package org.tmt.sbt.docs

import sbt.Keys._
import sbt.{File, _}

/**
 * Configures GhpagesPlugin to publish documents to user provided git repo inside provided directory
 */
object GithubPublishPlugin extends AutoPlugin {
  import com.typesafe.sbt.SbtGit.GitKeys
  import com.typesafe.sbt.sbtghpages.GhpagesPlugin
  import GhpagesPlugin.autoImport._

  override def requires: Plugins = GhpagesPlugin

  override def trigger = noTrigger

  object autoImport {
    val docsRepo       = settingKey[String]("The remote git repository where documents will be published")
    val docsParentDir  = settingKey[String]("Parent directory where generated documentation will be published")
    val gitCurrentRepo = settingKey[String]("The remote git repository associated with this project")
  }

  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    ghpagesBranch := "master",
    includeFilter in ghpagesCleanSite := { pathname: File =>
      pathname.getAbsolutePath.contains(s"${docsParentDir.value}/${version.value}")
    },
    GitKeys.gitRemoteRepo := docsRepo.value
  )
}
