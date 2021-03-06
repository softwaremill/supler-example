package com.softwaremill.supler_example

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.eclipse.jetty.webapp.WebAppContext

object SuplerExample extends App with EmbeddedJetty with LazyLogging {
  val embeddedJettyConfig = new EmbeddedJettyConfig {
    def rootConfig = ConfigFactory.load()
  }

  protected def setResourceBase(context: WebAppContext) {
    val webappDirInsideJar = context.getClass.getClassLoader.getResource("webapp").toExternalForm
    context.setWar(webappDirInsideJar)
  }

  startJetty()
  logger.info(s"Bootzooka started on $jettyAddress")

  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run() {
      stopJetty()
      logger.info("Bootzooka stopped")
    }
  })
}
