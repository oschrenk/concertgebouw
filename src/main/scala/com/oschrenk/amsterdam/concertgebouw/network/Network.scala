package com.oschrenk.amsterdam.concertgebouw.network

import com.typesafe.scalalogging.LazyLogging
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

class Network() extends LazyLogging {

  private val browser = JsoupBrowser()

  private val host = "https://www.concertgebouw.nl"

  def lunchtimeConcerts(): Document = {
    val doc = browser.get("https://www.concertgebouw.nl/en/concerts-tickets/special=Free+Lunchtime+Concerts")
    logger.trace(doc.toHtml)
    doc
  }

  def concertDetails(path: String): Document = {
    val doc = browser.get(s"$host$path")
    logger.trace(doc.toHtml)
    doc
  }

}
