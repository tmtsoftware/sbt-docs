package org.tmt.sbt.docs

import sbt.Def.settingKey

object DocKeys {
  val docsRepo       = settingKey[String]("The remote git repository where documents will be published")
  val docsParentDir  = settingKey[String]("Parent directory where generated documentation will be published")
  val gitCurrentRepo = settingKey[String]("The remote git repository associated with this project")
}
