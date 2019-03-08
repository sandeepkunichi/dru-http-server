package com.dru.api

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{ get, post }
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import com.dru.JsonSupport
import com.dru.api.SensorRegistryActor.{ ActionPerformed, CreateReading, GetReadings }

import scala.concurrent.Future
import scala.concurrent.duration._

trait SensorRoutes extends JsonSupport {
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[SensorRoutes])

  def sensorRegistryActor: ActorRef

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val sensorRoutes: Route =
    pathPrefix("readings") {
      pathEnd {
        concat(
          get {
            val readings: Future[Readings] = (sensorRegistryActor ? GetReadings).mapTo[Readings]
            complete(readings)
          },
          post {
            entity(as[Reading]) { reading =>
              val readingCreated: Future[ActionPerformed] =
                (sensorRegistryActor ? CreateReading(reading)).mapTo[ActionPerformed]
              onSuccess(readingCreated) { performed =>
                log.info("Created reading [{}]: {}", reading.a, performed.description)
                complete((StatusCodes.Created, performed))
              }
            }
          })
      }
    }

}
