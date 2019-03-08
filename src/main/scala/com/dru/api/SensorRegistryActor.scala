package com.dru.api

import akka.actor.{ Actor, ActorLogging, Props }

final case class Reading(a: Long, b: Long, c: Long, d: Long, e: Long, f: Long, g: Long, h: Long)
final case class Readings(readings: Seq[Reading])

object SensorRegistryActor {
  final case class ActionPerformed(description: String)
  final case object GetReadings
  final case class CreateReading(reading: Reading)

  def props: Props = Props[SensorRegistryActor]
}

class SensorRegistryActor extends Actor with ActorLogging {
  import SensorRegistryActor._

  var sensorData = Set.empty[Reading]

  def receive: Receive = {
    case GetReadings =>
      sender() ! Readings(sensorData.toSeq)
    case CreateReading(reading) =>
      sensorData += reading
      sender() ! ActionPerformed(s"Reading ${reading.a} created.")
  }
}
