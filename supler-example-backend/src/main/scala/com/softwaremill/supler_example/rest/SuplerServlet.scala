package com.softwaremill.supler_example.rest

import org.supler.FormWithObject


class SuplerServlet extends JsonServlet {
  override def mappingPath = SuplerServlet.MappingPath

  import org.supler.Supler._

  val person = Person("Tomek", "Szymanski", 31)

  val personForm = form[Person](f => List(
    f.field(_.firstName),
    f.field(_.lastName),
    f.field(_.age)
  ))

  get("/personform") {
    personForm(person).generateJSON
  }

  post("/personform") {
    val form = personForm(person).process(parsedBody)
    println(form.asInstanceOf[FormWithObject[Person]].obj)
    form.generateJSON
  }
}

object SuplerServlet {
  val MappingPath = "supler"
}

case class Person(firstName: String, lastName: String, age: Int)