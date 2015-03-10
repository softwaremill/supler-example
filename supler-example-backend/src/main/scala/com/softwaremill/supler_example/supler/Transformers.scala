package com.softwaremill.supler_example.supler

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.supler.Supler._
import org.supler.transformation.StringTransformer

object Transformers {
  implicit val dateTimeTransformer = new StringTransformer[DateTime] {
    override def serialize(t: DateTime) = ISODateTimeFormat.date().print(t)

    override def deserialize(u: String) = try {
      Right(ISODateTimeFormat.date().parseDateTime(u))
    } catch {
      case e: IllegalArgumentException => Left("error_custom_illegalDateFormat")
    }

    override def renderHint = Some(asDate())
  }
}
