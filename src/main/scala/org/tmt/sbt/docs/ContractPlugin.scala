package org.tmt.sbt.docs

import com.typesafe.sbt.site.SitePlugin
import com.typesafe.sbt.site.util.SiteHelpers.addMappingsToSiteDir
import sbt.Keys._
import sbt.{Def, _}

import scala.language.postfixOps

object ContractPlugin extends AutoPlugin {

  override def requires: Plugins = SitePlugin

  object autoImport {
    val generateContract        = taskKey[Seq[(File, String)]]("generate contracts")
    val generateContractDirName = "contracts"
    val generateContractDirPath = settingKey[String]("path of the folder for the generated contracts")
  }
  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    generateContractDirPath := "/" + generateContractDirName,
    addMappingsToSiteDir(generateContract, generateContractDirPath)
  )

  def generate(generatorProject: Project): Def.Initialize[Task[Seq[(File, String)]]] = Def.taskDyn {
    val outputDir   = s" ${target.value}/$generateContractDirName"
    val resourceDir = "src/main/resources"
    Def.task {
      (generatorProject / Compile / run).toTask(s" $outputDir $resourceDir").value
      Path.contentOf(target.value / generateContractDirName)
    }
  }
}
