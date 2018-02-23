package com.oschrenk.amsterdam.concertgebouw

import com.fasterxml.jackson.core.{JsonFactory, JsonParser}
import com.fasterxml.jackson.databind.ObjectMapper
import io.circe.jackson.CirceJsonModule
import org.scalatest.{FlatSpec, Matchers}

class JacksonSpec extends FlatSpec with Matchers {

  "Jackson" should "ignore trailing commas" in {
    val mapper = new ObjectMapper
    val jsonFactory: JsonFactory = new JsonFactory(mapper)
    mapper.enable(JsonParser.Feature.ALLOW_TRAILING_COMMA)
    mapper.registerModule(CirceJsonModule)

    println(mapper.isEnabled(JsonParser.Feature.ALLOW_TRAILING_COMMA))

    val parser = jsonFactory.createParser(rawSchema)
    val sd = mapper.readValue(parser, classOf[Json])
  }

}