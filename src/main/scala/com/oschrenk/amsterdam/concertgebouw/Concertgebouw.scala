package com.oschrenk.amsterdam.concertgebouw

import com.oschrenk.amsterdam.concertgebouw.network.{Extractors, Network}

object Concertgebouw extends App {

  val network = new Network()

  val concerts = network.lunchtimeConcerts()
  val details = Extractors.concerts(concerts).take(1)
    .map(_.path)
    .map(network.concertDetails)
    .map(Extractors.singleConcert)
  println(details)

}
