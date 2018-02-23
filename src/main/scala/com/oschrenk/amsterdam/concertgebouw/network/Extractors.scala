package com.oschrenk.amsterdam.concertgebouw.network

import com.typesafe.scalalogging.LazyLogging

object Extractors extends LazyLogging {

  case class Concert(title: String, artist: String, path: String)
  case class ConcertDetails(start: String, end: String)

  def concerts(json: Json): Seq[Concert] = {

    val listSelector = s"main > div > div > section > div > section:nth-child(2) > div.flexblock.event-list.event-list--default > ul > li"
    val cells = document >> elementList(listSelector)
    cells.map { cell =>
      val linkSelector = "div > div.event-result__content > a"
      val metaSelector = "div > div > div.event-result__meta.event-meta"
      val artistSelector = "div > div.event-meta__artists--short"
      val head = cell >> element(linkSelector)
      val meta = cell >> element(metaSelector)
      val artistShort = meta >> element(artistSelector)

      val title = head >> text("h2")
      val link = head.attr("href")
      val artist = artistShort >> text

      Concert(title, artist, link)
    }
  }

  case class StructuredData(startDate: String)

  def singleConcert(document: Document): ConcertDetails = {
    (document >> elementList("head > script")).takeRight(3).take(2) match {
      case data :: schema :: _ =>

//        JsoupElement(<script>
//          dataLayer = (typeof dataLayer !== 'undefined' ? dataLayer : []);
//          dataLayer.push(
//          {"visitorId":"","pageType":"EventPage","ecommerce":{"detail":{"products":[{"name":"Free Lunchtime Concert","id":84749,"event_guid":"52e051bb-4694-4c5f-b649-871689b65897","price":0,"brand":"Early Music|Organ|Jeroen Koopman|Het Concertgebouw Eigen Programmering|Main Hall||","list":"EventPage","category":"Free Lunchtime Concerts|The Concertgebouw presents","position":1,"variant":"event","concert":{"genre":"Early Music","instrument":"Organ","uitvoerenden":"Jeroen Koopman","organisator":"Het Concertgebouw Eigen Programmering","ruimte":"Main Hall","componist":null}}]}}}
//    );
//    </script>)
        val rawData = data.toString.split("\n").slice(3, 4).head

//        JsoupElement(<script type="application/ld+json">
//          {
//          "@context": "http://schema.org",
//          "@type": "MusicEvent",
//    "name": "Free Lunchtime Concert",
//    "startDate": "2018-03-02T12:30",
//    "endDate": "2018-03-02T13:00",
//    "url": "https://www.concertgebouw.nl/en/page/34432",
//    "location":
//    {
//    "@type": "MusicVenue",
//    "name": "Het Concertgebouw",
//    "address": "Concertgebouwplein 10, 1071 LN Amsterdam, The Netherlands"
//    },
//    "offers":
//    {
//    "@type": "Offer",
//    "url": "https://www.concertgebouw.nl/en/page/34432",
//    "category": "primary",
//    "price": "Free",
//    "priceCurrency": "EUR",
//    "availability": "http://schema.org/SoldOut"
//    },
//    "performer":
//    [
//    { "@type": "Person", "name": "Jeroen Koopman" }            ],
//    }
//
//    </script>)

    val rawSchema = schema.toString.split("\n").drop(1).dropRight(1).mkString("\n")
        println(rawSchema)
        import com.fasterxml.jackson.core.JsonParser
        import com.fasterxml.jackson.databind.ObjectMapper
        val mapper = new ObjectMapper
        mapper.registerModule(CirceJsonModule)

        val jsonFactory: JsonFactory = new JsonFactory(mapper)
        jsonFactory.enable(JsonParser.Feature.ALLOW_TRAILING_COMMA)

        val parser = jsonFactory.createParser(rawSchema)
        val sd = mapper.readValue(parser, classOf[Json])

        println("AAAAh" + sd)

    val foo = jackson.decode[StructuredData](rawSchema)
    println("AAAAh" + foo)
        println(rawData)
        println(rawSchema)
      case _ => throw new IllegalArgumentException("Aaaargh")
    }

    ConcertDetails("s", "e")
  }
}


