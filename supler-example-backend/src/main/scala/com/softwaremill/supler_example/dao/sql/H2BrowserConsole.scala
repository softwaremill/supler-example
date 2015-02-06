package com.softwaremill.supler_example.dao.sql

import com.softwaremill.supler_example.dao.DaoConfig
import com.typesafe.config.ConfigFactory

object H2BrowserConsole extends App {
  val config = new DaoConfig {
    def rootConfig = ConfigFactory.load()
  }

  new Thread(new Runnable {
    def run() = new org.h2.tools.Console().runTool("-url", SqlDatabase.connectionString(config))
  }).start()

  println("The console is now running in the background.")
}
