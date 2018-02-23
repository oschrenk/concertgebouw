package com.oschrenk.amsterdam.concertgebouw

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import org.http4s.EntityDecoder
import org.http4s.Status.Successful
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import org.http4s.dsl.io._

object Network {
  private val BastardDateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

  import io.circe.java8.time.decodeLocalDateTime
  import io.circe.Decoder
  import io.circe.generic.semiauto.deriveDecoder

  implicit val decodeLocalDateTimeDefault: Decoder[LocalDateTime] = decodeLocalDateTime(BastardDateTimeFormat)
  implicit val concertDecoder: Decoder[Concert] = deriveDecoder[Concert]
  implicit val entityDecoder: EntityDecoder[IO, Seq[Concert]] = jsonOf[IO, Seq[Concert]]
}

class Network(client: Client[IO]) extends LazyLogging {
  import Network._

  def lunchtimeConcerts(): IO[Seq[Concert]] = {
    client.get(uri("https://www.concertgebouw.nl/feed/en/events.json?special=Free+Lunchtime+Concerts")) {
      case Successful(resp) => resp.as[Seq[Concert]]
      case NotFound(_) => IO.pure(Seq.empty)
      case _ => IO.pure(Seq.empty)
    }
  }
}
