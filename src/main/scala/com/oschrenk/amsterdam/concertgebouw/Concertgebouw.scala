package com.oschrenk.amsterdam.concertgebouw

import cats.effect.IO
import org.http4s.client.{Client, blaze}

object Concertgebouw extends App {

  val client: Client[IO] = blaze.Http1Client[IO]().unsafeRunSync()

  new Network(client).lunchtimeConcerts().unsafeRunSync().foreach( concert =>
    println(concert)
  )

  client.shutdownNow()
}
