package gosigma.scala

import java.util.{ Date, Locale }
import java.text.DateFormat
import java.text.DateFormat._

object Date {
  def main(args: Array[String]) {
    val now = new Date
    val df = getDateInstance(LONG, Locale.CANADA)
    println(df format now)
  }
}
