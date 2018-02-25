package com.oschrenk.amsterdam.concertgebouw

import cats.effect.IO
import java.time._
import ical.Event
import org.http4s.client.{Client, blaze}

object Concertgebouw extends App {

  private val client: Client[IO] = blaze.Http1Client[IO]().unsafeRunSync()
  private val zoneId = ZoneId.of("Europe/Amsterdam")

  new Network(client).lunchtimeConcerts().unsafeRunSync().foreach{ concert =>
    val start = concert.event_start_date.atZone(zoneId)
    val end = concert.event_end_date.atZone(zoneId)
    val event = Event.from(concert.title, start, end, None)
    println(event)
  }

  client.shutdownNow()
}
