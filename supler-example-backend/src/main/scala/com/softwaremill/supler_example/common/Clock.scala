package com.softwaremill.supler_example.common

import org.joda.time.{DateTimeZone, DateTime}

trait Clock {
  def now: DateTime
  def nowUtc: DateTime
  def nowMillis: Long
}

object RealTimeClock extends Clock {
  def now = DateTime.now()
  def nowUtc = DateTime.now(DateTimeZone.UTC)
  def nowMillis = System.currentTimeMillis()
}

class FixtureTimeClock(millis: Long) extends Clock {
  def now = new DateTime(millis)
  def nowUtc = new DateTime(millis, DateTimeZone.UTC)
  def nowMillis = millis
}
