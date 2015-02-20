package com.softwaremill.supler_example.rest

import java.util.UUID

import com.softwaremill.supler_example.dao.person.PersonDao
import com.softwaremill.supler_example.domain.Person
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.json4s.JsonAST.JString
import org.supler.transformation.StringTransformer
import org.supler.{Message, FormWithObject}
import org.supler.field.ActionResult

import com.github.nscala_time.time.Imports._


class SuplerServlet(val personDao: PersonDao) extends JsonServlet {
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

  val person = Person(UUID.randomUUID(), "Tomek", "Szymanski", "tom@email.com", new DateTime(1983, 4, 6, 0, 0), None)

  val personForm = form[Person](f => List(
    f.field(_.name).label("Name"),
    f.field(_.lastName).label("Last Name"),
    f.field(_.email).label("E-mail"),
    f.field(_.dob).label("Date of Birth"),
    f.field(_.address).label("Address"),
    f.staticField(p => Message(if ((p.dob + 30.years) < DateTime.now) "You're old" else "You're young")),
    f.action("save") { p =>
      println("Saving person: " + p)
      personDao.addPerson(p)
      ActionResult.custom(JString("Saved"))
    }.label("Save").validateAll()
  ))

  val personROForm = form[Person](f => List(
    f.staticField(p => Message(p.name)).label("Name"),
    f.staticField(p => Message(p.lastName)).label("Last Name"),
    f.staticField(p => Message(p.email)).label("E-mail"),
    f.staticField(p => Message(p.dob)).label("Date of Birth"),
    f.staticField(p => Message(p.address.getOrElse(""))).label("Address")
  ))

  case class People(people: List[Person])

  val personListForm = form[People](f => List(
    f.subform(_.people, personROForm).label("People").renderHint(asTable())
  ))

  get("/personform") {
    personForm(person).withMeta("id", person.id.toString).generateJSON
  }

  post("/personform") {
    val entityId = personForm.getMeta(parsedBody)("id")
    println(s"Reading person with ID $entityId")
    personForm(person).process(parsedBody).generateJSON
  }

  get("/personlist") {
    personListForm(People(personDao.loadAll)).generateJSON
  }
}

object SuplerServlet {
  val MappingPath = "supler"
}

