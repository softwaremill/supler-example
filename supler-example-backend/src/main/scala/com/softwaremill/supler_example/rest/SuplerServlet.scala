package com.softwaremill.supler_example.rest

import org.json4s.JsonAST.JString
import org.supler.FormWithObject
import org.supler.field.ActionResult


class SuplerServlet extends JsonServlet {
  override def mappingPath = SuplerServlet.MappingPath

  import org.supler.Supler._

  val person = Person("Tomek Szymanski", 31, None, true)

  val personForm = form[Person](f => List(
    f.field(_.name),
    f.field(_.age),
    f.field(_.address).label("Address"),
    f.field(_.likesChocolate).label("Do you like chocolate?"),
    f.action("save") { p =>
      println("Saving person: " + p)
      ActionResult.custom(JString("Saved"))
    }.label("Save").validateAll()
  ))

  get("/personform") {
    personForm(person).generateJSON
  }

  post("/personform") {
    personForm(person).process(parsedBody).generateJSON
  }
}

object SuplerServlet {
  val MappingPath = "supler"
}

case class Person(name: String, age: Int, address: Option[String], likesChocolate: Boolean)