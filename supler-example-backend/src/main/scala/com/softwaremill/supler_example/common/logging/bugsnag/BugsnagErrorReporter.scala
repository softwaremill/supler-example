package com.softwaremill.supler_example.common.logging.bugsnag

import ch.qos.logback.classic.spi.ILoggingEvent
import com.bugsnag.{MetaData, Client}
import com.softwaremill.supler_example.common.config.ConfigWithDefault
import com.softwaremill.supler_example.common.logging.{DummyErrorReporter, AsyncErrorReportingLogAppender, ErrorReporter}
import com.typesafe.config.Config
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * Simple wrapper for Bugsnag's Client API
 * @param apiKey valid api key. Must be non-null.
 */
class BugsnagErrorReporter(private val apiKey: String) extends ErrorReporter {

  private val bugsnagClient = new Client(apiKey)

  override def report(ex: Throwable, loggingEvent: ILoggingEvent): Unit = {
    val metaData = new MetaData()
    metaData.addToTab("Exception message", loggingEvent.getMessage)
    bugsnagClient.notify(ex, metaData)
  }

}

object BugsnagErrorReporter extends LazyLogging {

  val configApiKeyPath = "errorReporting.bugsnag.apiKey"

  /**
   * Create BugsnagErrorReporter instance
   *
   * @param config an instance of configuration holder
   * @return BugsnagErrorReporter if api key is present and is non empty or DummyErrorReporter otherwise
   */
  def apply(config: ConfigWithDefault): ErrorReporter =
    config.getOptionalString(configApiKeyPath)
      .filterNot(_.isEmpty)
      .map(new BugsnagErrorReporter(_))
      .getOrElse {
        logger.info("Missing or invalid Bugsnag API Key - falling back to DummyErrorReporter.")
        DummyErrorReporter
      }

}
