package org.tmt.sbt.docs

import de.heikoseeberger.sbtheader.{AutomateHeaderPlugin, CommentCreator, CommentStyle, HeaderPlugin}
import sbt.Keys.*
import sbt.*

object TMTCopyrightHeaderPlugin extends AutoPlugin {
  import HeaderPlugin.autoImport._
  val currentYear = "2022"

  override def requires: Plugins = HeaderPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Setting[_]] = {
    AutomateHeaderPlugin.projectSettings ++
    Seq(
      headerSources / includeFilter := "*.scala" || "*.java" || "*.kt" || "*.kts",
      headerLicense                 := Some(
        HeaderLicense.Custom(
          s"""|Copyright (c) $currentYear ${organizationName.value}
              |SPDX-License-Identifier: Apache-2.0
              |""".stripMargin
        )
      ),
      headerMappings                := headerMappings.value ++ Map(
        HeaderFileType.scala  -> cStyleComment,
        HeaderFileType.java   -> cStyleComment,
        HeaderFileType("kt")  -> cStyleComment,
        HeaderFileType("kts") -> cStyleComment
      )
    )
  }

  val cStyleComment = CommentStyle.cStyleBlockComment.copy(commentCreator = new CommentCreator() {
    val Pattern                                                            = "(?s).*?(\\d{4}(-\\d{4})?).*".r
    def findYear(header: String): Option[String]                           = header match {
      case Pattern(years, _) => Some(years)
      case _                 => None
    }
    override def apply(text: String, existingText: Option[String]): String = {
      val newText = CommentStyle.cStyleBlockComment.commentCreator.apply(text, existingText)
      existingText
        .flatMap(findYear)
        .map(year => newText.replace(currentYear, year))
        .getOrElse(newText)
    }
  })
}
