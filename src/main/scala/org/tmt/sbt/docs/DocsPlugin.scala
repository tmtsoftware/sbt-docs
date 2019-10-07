package org.tmt.sbt.docs

import sbt._

/**
 * Configures paradox with material theme and enables github publish
 * Combines GithubPublishPlugin and ParadoxMaterialSitePlugin
 * @note It does not include unidoc settings (scaladoc and javadoc), use UnidocSitePlugin for api docs
 */
object DocsPlugin extends AutoPlugin {
  override def trigger                          = noTrigger
  override def requires: Plugins                = GithubPublishPlugin && ParadoxMaterialSitePlugin
  override def projectSettings: Seq[Setting[_]] = Settings.makeSiteMappings()
}
