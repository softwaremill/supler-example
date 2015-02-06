package com.softwaremill.supler_example.test

import org.scalatest.BeforeAndAfterEach

trait ClearSqlDataAfterEach extends BeforeAndAfterEach {
  this: FlatSpecWithSql =>

  override protected def afterEach() {
    try {
      clearData()
    } catch {
      case e: Exception => e.printStackTrace()
    }

    super.afterEach()
  }
}
