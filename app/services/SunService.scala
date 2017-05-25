package services

import scala.concurrent.Future
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import play.api.libs.ws.WS
import play.api.Play.current
import play.api.libs.ws.{WSClient, WS}
import scala.concurrent.ExecutionContext.Implicits.global
import model.SunInfo

class SunService(wsClient: WSClient) {
  def getSunInfo(lat: Double, lon: Double) : Future[SunInfo] = {
    val responseF = wsClient.url(
      "http://api.sunrise-sunset.org/json?" +
        "lat=46.5197&lng=6.6323&formatted=0"
    ).get()

    responseF.map { response =>
      val json = response.json
      val sunriseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunsetTimeStr  = (json \ "results" \ "sunset").as[String]
      val sunriseTime    = DateTime.parse(sunriseTimeStr)
      val sunsetTime     = DateTime.parse(sunsetTimeStr)
      val formatter      = DateTimeFormat.forPattern("HH:mm::ss").
        withZone(DateTimeZone.forID("Europe/Zurich"))

      SunInfo(formatter.print(sunriseTime), formatter.print(sunsetTime))
    }
  }
}
