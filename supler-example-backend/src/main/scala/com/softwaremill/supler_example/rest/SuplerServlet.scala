package com.softwaremill.supler_example.rest

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.json4s.JsonAST.JString
import org.supler.transformation.StringTransformer
import org.supler.{Message, FormWithObject}
import org.supler.field.ActionResult

import com.github.nscala_time.time.Imports._


class SuplerServlet extends JsonServlet {
  override def mappingPath = SuplerServlet.MappingPath

  import org.supler.Supler._

  implicit val dateTimeTransformer = new StringTransformer[DateTime] {
    override def serialize(t: DateTime) = ISODateTimeFormat.date().print(t)

    override def deserialize(u: String) = try {
      Right(ISODateTimeFormat.date().parseDateTime(u))
    } catch {
      case e: IllegalArgumentException => Left("error_custom_illegalDateFormat")
    }
  }

  val person = Person("Tomek", "Szymanski", new DateTime(1983, 4, 6, 0, 0), None)

  val personForm = form[Person](f => List(
    f.field(_.name),
    f.field(_.lastName),
    f.field(_.dob),
    f.field(_.address).label("Address"),
    f.staticField(p => Message(if ((p.dob + 30.years) < DateTime.now) "You're old" else "You're young")),
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

case class Person(name: String, lastName: String, dob: DateTime, address: Option[String])