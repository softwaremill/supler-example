package com.softwaremill.supler_example.form

import java.util.UUID

import com.softwaremill.supler_example.domain.Person
import org.joda.time.DateTime

case class PersonForm(id: UUID, name: String, lastName: String, email: String,
                 dob: DateTime, address: Option[String], password: String, repeatPassword: String) {
  def toPerson: Person = Person(id, name, lastName, email, dob, address, password)
}

object PersonForm {
  def apply(p: Person): PersonForm = PersonForm(p.id, p.name, p.lastName, p.email, p.dob, p.address, "", "")
}
