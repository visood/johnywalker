package controllers

import scala.concurrent.Future
import play.api._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import model.SunInfo
import services.{SunService, WeatherService}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
class Application extends Controller {

  val sunService     = new SunService
  val weatherService = new WeatherService

  def index = Action.async {
    val lat = 46.5197
    val lon = 6.6323

    val sunInfoF     = sunService.getSunInfo(lat, lon)
    val temperatureF = weatherService.getTemperature(lat, lon)
                     
    for {
      sunInfo     <- sunInfoF
      temperature <- temperatureF
    } yield {
      Ok(views.html.index(sunInfo, temperature))
    }
  }
}
