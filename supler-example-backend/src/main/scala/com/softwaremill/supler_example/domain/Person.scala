package com.softwaremill.supler_example.domain

import java.util.UUID

import org.joda.time.DateTime

case class Person(id: UUID, name: String, lastName: String, email: String,
                  dob: DateTime, address: Option[String], password: String)
