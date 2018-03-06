package com.oschrenk.amsterdam.concertgebouw

import java.time.LocalDateTime

case class Concert(
  title: String,
  event_start_date: LocalDateTime,
  event_end_date: LocalDateTime,
  tag_musician: Seq[String],
  url: String,
  tag_instrument: Option[Seq[String]]
)
