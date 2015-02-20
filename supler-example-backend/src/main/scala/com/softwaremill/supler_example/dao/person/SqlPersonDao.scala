package com.softwaremill.supler_example.dao.person

import com.softwaremill.supler_example.dao.sql.SqlDatabase
import com.softwaremill.supler_example.domain.Person

class SqlPersonDao(protected val database: SqlDatabase) extends PersonDao with SqlPersonSchema {

  import database._
  import database.driver.simple._

  override def loadAll = db.withSession { implicit session =>
    persons.list
  }

  override def findOne(id: PersonId) = db.withSession { implicit session =>
    persons.filter(_.id === id).firstOption
  }

  override def addPerson(person: Person) = db.withTransaction { implicit session =>
    persons += person
    person
  }
}
