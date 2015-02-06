package com.softwaremill.supler_example.service.email

import com.softwaremill.supler_example.service.templates.EmailContentWithSubject

trait EmailScheduler {

  def scheduleEmail(address: String, emailData: EmailContentWithSubject)

}
