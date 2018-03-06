package com.oschrenk.amsterdam.concertgebouw

import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import org.http4s.Status.Successful
import org.http4s.client.Client
import org.http4s.dsl.io._

class Network(client: Client[IO]) extends LazyLogging {

  def lunchtimeConcerts(): IO[Option[String]] = {
    client.get(uri("https://www.concertgebouw.nl/feed/en/events.json?special=Free+Lunchtime+Concerts")) {
      case Successful(resp) =>
        resp.as[String].map(Option.apply)
      case NotFound(_) =>
        IO.pure(None)
      case _ =>
        IO.pure(None)
    }
  }
}
