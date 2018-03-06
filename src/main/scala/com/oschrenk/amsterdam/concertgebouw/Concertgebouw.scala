package com.oschrenk.amsterdam.concertgebouw

import cats.effect.IO
import java.time._
import java.time.format.DateTimeFormatter

import ical.{Event, Writer}
import org.http4s.client.{Client, blaze}

object Concertgebouw extends App {

  private val BastardDateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

  import io.circe.java8.time.decodeLocalDateTime
  import io.circe.Decoder
  import io.circe.generic.semiauto.deriveDecoder
  implicit val decodeLocalDateTimeDefault: Decoder[LocalDateTime] = decodeLocalDateTime(BastardDateTimeFormat)
  implicit val concertDecoder: Decoder[Concert] = deriveDecoder[Concert]

  private val client: Client[IO] = blaze.Http1Client[IO]().unsafeRunSync()
  private val zoneId = ZoneId.of("Europe/Amsterdam")

  new Network(client).lunchtimeConcerts().unsafeRunSync().foreach{ rawConcert =>
    import io.circe.parser._
    parse(rawConcert).flatMap(_.as[Seq[Concert]]).map { concerts =>
      concerts.map{ concert =>
        val start = concert.event_start_date.atZone(zoneId)
        val end = concert.event_end_date.atZone(zoneId)
        Event.from(concert.title, start, end, None)
      }
    } match {
      case Left(e) =>
        println(e)
      case Right(concerts) =>
        concerts.foreach{ c =>
          println(Writer.asIcal(c))
        }
    }
  }

  client.shutdownNow()
}
