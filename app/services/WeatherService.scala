package services

import scala.concurrent.Future
import play.api.libs.ws.WS
import play.api.Play.current
import play.api.libs.ws.{WSClient, WS}
import scala.concurrent.ExecutionContext.Implicits.global

class WeatherService(wsClient: WSClient)
{
  def getTemperature(lat: Double, lon: Double) : Future[Double] = {
    val owid: String = "da87f848cd1329bf98f8f52b62c511a3"
    val weatherResponseF = wsClient.url(
      "http://api.openweathermap.org/data/2.5/" +
        "weather?lat=46.5197&lon=6.6323&units=metric&APPID=" + owid
    ).get()

    weatherResponseF.map { weatherResponse =>
      val weatherJson  = weatherResponse.json
      val temperature  = (weatherJson \ "main" \ "temp").as[Double]

      temperature
    }
  }
}
