package com.softwaremill.supler_example.service.email

import org.scalatest.{Matchers, FunSpec}
import com.softwaremill.supler_example.service.templates.EmailContentWithSubject

class DummyEmailSendingServiceSpec extends FunSpec with Matchers {
  describe("Dummy email sending service") {
    it("send scheduled email") {
      val service = new DummyEmailSendingService
      service.scheduleEmail("test@sml.com", new EmailContentWithSubject("content", "subject"))
      service.run()
      service.wasEmailSent("test@sml.com", "subject") should be(true)
    }
  }
}
