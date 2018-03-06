package com.oschrenk.amsterdam.concertgebouw

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.time._
import java.time.format.DateTimeFormatter

import cats.effect.IO
import ical.{Event, Writer}
import org.http4s.client.{Client, blaze}

object Concertgebouw extends App {

  private val BastardDateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

  import io.circe.Decoder
  import io.circe.generic.semiauto.deriveDecoder
  import io.circe.java8.time.decodeLocalDateTime
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
        val title = {
          val mainTitle = concert.title
          mainTitle + "w/"+ concert.tag_musician.head
        }
        val notes = {
          val instruments = concert.tag_instrument.map(_.mkString(",")).getOrElse("")
          val musicians = concert.tag_musician.mkString(",")
          musicians + "" + instruments
        }
        Event.from(title, start, end, Some(notes))
      }
    } match {
      case Left(e) =>
        println(e)
      case Right(concerts) =>
        concerts.foreach{ c =>
          val filename = {
            val epoch = c.events.head.dtstart.get.value.value.left.get.dt.toEpochSecond
            val title = c.events.head.summary.get.value.text.toLowerCase()
            s"$epoch-$title".replaceAll("[^a-zA-Z0-9.-]", "_") + ".ics"
          }
          val homeDirectory = System.getProperty("user.home")
          val outputDirectory = new File(homeDirectory, "Downloads").getCanonicalPath
          val path = new File(outputDirectory, filename).toPath
          val content = Writer.asIcal(c)
          Files.write(path, content.getBytes(StandardCharsets.UTF_8))
        }
    }
  }

  client.shutdownNow()
}
