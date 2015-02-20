package com.softwaremill.supler_example.dao.person

import java.util.UUID

import com.softwaremill.supler_example.domain.Person

trait PersonDao {

  type PersonId = UUID

  def loadAll: List[Person]

  def addPerson(person: Person): Person

  def findOne(id: PersonId): Option[Person]
}
