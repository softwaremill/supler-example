package com.softwaremill.supler_example

import com.softwaremill.supler_example.rest.swagger.BootzookaSwagger

package object rest {

  implicit val bootzookaSwagger = new BootzookaSwagger
}
