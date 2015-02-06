package com.softwaremill.supler_example.service.config

import com.softwaremill.supler_example.common.config.ConfigWithDefault
import com.typesafe.config.Config

trait SuplerExampleConfig extends ConfigWithDefault {
  def rootConfig: Config

  private lazy val bootzookaConfig = rootConfig.getConfig("bootzooka")

  lazy val bootzookaResetLinkPattern = getString("bootzooka.reset-link-pattern", "http://localhost:8080/#/password-reset?code=%s")
}
