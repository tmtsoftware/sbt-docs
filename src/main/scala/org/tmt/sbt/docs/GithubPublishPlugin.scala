package org.tmt.sbt.docs

import org.tmt.sbt.docs.DocKeys._
import sbt.Keys._
import sbt._

/**
 * Configures GhpagesPlugin to publish documents to user provided git repo inside provided directory
 */
object GithubPublishPlugin extends AutoPlugin {
  import com.typesafe.sbt.SbtGit.GitKeys
  import com.typesafe.sbt.sbtghpages.GhpagesPlugin
  import GhpagesPlugin.autoImport._

  override def requires: Plugins = GhpagesPlugin

  override def trigger = noTrigger

  override def projectSettings: Seq[Setting[_]] = Seq(
    ghpagesBranch                    := "master",
    ghpagesCleanSite / includeFilter := { pathname: File =>
      pathname.getAbsolutePath.contains(s"${docsParentDir.value}/${version.value}")
    },
    GitKeys.gitRemoteRepo            := docsRepo.value
  )
}
