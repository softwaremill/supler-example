package com.softwaremill.supler_example.rest

import java.util.{Objects, UUID}

import com.softwaremill.supler_example.dao.person.PersonDao
import com.softwaremill.supler_example.domain.Person
import com.softwaremill.supler_example.form.PersonForm
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.json4s.JsonAST.{JField, JObject, JString}
import org.supler.Message
import org.supler.field.ActionResult
import org.supler.transformation.StringTransformer


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

    override def renderHint = Some(asDate())
  }

  val personForm = form[PersonForm](f => List(
    f.field(_.name).label("Name") || f.field(_.lastName).label("Last Name"),
    f.field(_.email).label("E-mail") || f.field(_.dob).label("Date of Birth"),
    f.field(_.address).label("Address"),
    f.field(_.password).label("Password").renderHint(asPassword()).validate(
      custom((pass, person) => Objects.equals(pass, person.repeatPassword), (pass, person) => Message("Passwords do not match")))
      || f.field(_.repeatPassword).renderHint(asPassword()),
    f.action("save") { p =>
      println("Saving person: " + p)
      personDao.addPerson(p.toPerson)
      ActionResult.custom(JString("Saved"))
    }.label("Save").validateAll()
  ))

  val personROForm = form[Person](f => List(
    f.field(_.id).renderHint(asHidden()),
    f.staticField(p => Message(p.name)).label("Name"),
    f.staticField(p => Message(p.lastName)).label("Last Name"),
    f.staticField(p => Message(p.email)).label("E-mail"),
    f.staticField(p => Message(p.dob)).label("Date of Birth"),
    f.staticField(p => Message(p.address.getOrElse(""))).label("Address"),
    f.action("Edit"){
      s => ActionResult.custom(JObject(JField("action", JString("edit")), JField("id", JString(s.id.toString))))
    }.label("Edit"),
    f.action("Delete"){
      s => ActionResult.custom(JObject(JField("action", JString("delete")), JField("id", JString(s.id.toString))))
    }.label("Delete")
  ))

  case class People(people: List[Person])

  val personListForm = form[People](f => List(
    f.subform(_.people, personROForm).label("People").renderHint(asTable())
  ))

  get("/personform") {
    val person = newPerson
    personForm(PersonForm(person)).withMeta("id", person.id.toString).generateJSON
  }

  get("/personform/:id") {
    val person = personDao.findOne(UUID.fromString(params("id"))) match {
      case Some(p) => p
      case None => newPerson
    }
    personForm(PersonForm(person)).withMeta("id", person.id.toString).generateJSON
  }

  post("/personform") {
    val entityId = UUID.fromString(personForm.getMeta(parsedBody)("id"))
    val person = personDao.findOne(entityId) match {
      case Some(p) => p
      case None => newPerson.copy(id = entityId) // the person is not saved yet
    }
    personForm(PersonForm(person)).process(parsedBody).generateJSON
  }

  delete("/person/:id") {
    val person = personDao.delete(UUID.fromString(params("id")))
  }

  get("/personlist") {
    personListForm(People(personDao.loadAll)).generateJSON
  }

  post("/personlist") {
    personListForm(People(personDao.loadAll)).process(parsedBody).generateJSON
  }

  private def newPerson = Person(UUID.randomUUID(), "", "", "", DateTime.now(), None, "")
}

object SuplerServlet {
  val MappingPath = "supler"
}

