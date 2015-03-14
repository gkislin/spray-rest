package domain

import java.lang.String
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatter}

/**
 * User: gkislin
 * Date: 03.10.13
 */
object TimeFormatter {
  val fmt: DateTimeFormatter = ISODateTimeFormat.dateTime()

  def getNow: String = {
    toISO(System.currentTimeMillis)
  }

  def toISO(ms: Long): String = {
    fmt.print(ms)
  }

  def toMs(isoDate: String): Long = {
    fmt.parseDateTime(isoDate).getMillis
  }
}
