package com.softwaremill.supler_example.form

import com.softwaremill.supler_example.dao.forms.TerroristDao
import com.softwaremill.supler_example.supler.Transformers
import org.joda.time.DateTime

case class Terrorist(areYou: Boolean, how: HowTerrorist)

case class HowTerrorist(bombsLaid: Int, bombsSucceeded: Int, crossedOnRedLight: Boolean, crossedOnRedLightCount: Int,
                         killedAFly: Boolean, killedAFlyCount: Int, flies: List[Fly])

case class Fly(date: DateTime, location: String)

class TerroristForm(dao: TerroristDao) {
  import org.supler.Supler._

  import Transformers._

  val flyForm = form[Fly](f => List(
    f.field(_.date),
    f.field(_.location)
  ))

  val howTerroristForm = form[HowTerrorist](f => List(
    f.field(_.bombsLaid) || f.field(_.bombsSucceeded).includeIf(_.bombsLaid > 0),
    f.field(_.crossedOnRedLight) || f.field(_.crossedOnRedLightCount).includeIf(_.crossedOnRedLight),
    f.field(_.killedAFly) || f.field(_.killedAFlyCount).includeIf(_.killedAFly),
    f.subform(_.flies, flyForm).renderHint(asTable()).includeIf(_.killedAFly)
  ))

  val terroristForm = form[Terrorist](f => List(
    f.field(_.areYou),
    f.subform(_.how, howTerroristForm).includeIf(_.areYou)
  ))
}