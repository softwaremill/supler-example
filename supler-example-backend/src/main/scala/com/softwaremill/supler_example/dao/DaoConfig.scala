package com.softwaremill.supler_example.dao

import com.softwaremill.supler_example.common.config.ConfigWithDefault
import com.typesafe.config.Config

trait DaoConfig extends ConfigWithDefault {
  def rootConfig: Config

  lazy val embeddedDataDir: String = getString("bootzooka.data-dir", "./data")
}
