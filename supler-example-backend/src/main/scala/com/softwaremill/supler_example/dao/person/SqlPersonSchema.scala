package com.softwaremill.supler_example.dao.person

import java.util.UUID

import com.softwaremill.supler_example.dao.sql.SqlDatabase
import com.softwaremill.supler_example.domain.{Person}
import org.joda.time.DateTime


trait SqlPersonSchema {

  protected val database: SqlDatabase

  import database._
  import database.driver.simple._

  protected val persons = TableQuery[Persons]

  protected class Persons(tag: Tag) extends Table[Person](tag, "persons") {
    def id = column[UUID]("id", O.PrimaryKey)
    def name = column[String]("name")
    def lastName = column[String]("last_name")
    def email = column[String]("email")
    def dob = column[DateTime]("dob")
    def address = column[Option[String]]("address")
    def password = column[String]("password")

    def * = (id, name, lastName, email, dob, address, password) <>(Person.tupled, Person.unapply)
  }

}
