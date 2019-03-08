package com.dru

import com.dru.api.SensorRegistryActor.ActionPerformed
import com.dru.api.{Reading, Readings}
import spray.json.RootJsonFormat

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit val readingJsonFormat: RootJsonFormat[Reading] = jsonFormat8(Reading)
  implicit val readingsJsonFormat: RootJsonFormat[Readings] = jsonFormat1(Readings)

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)
}
