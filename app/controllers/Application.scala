package controllers

import java.util.concurrent.TimeUnit

import actors.StatsActor
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.mvc._
import play.api.Play.current

import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import model.SunInfo
import services.{SunService, WeatherService}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
class Application(sunService: SunService,
                  weatherService: WeatherService,
                  actorSystem: ActorSystem) extends Controller
{

  def index = Action.async {
    val lat = 46.5197
    val lon = 6.6323

    val sunInfoF     = sunService.getSunInfo(lat, lon)
    val temperatureF = weatherService.getTemperature(lat, lon)

    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    val requestsF = (
      actorSystem.actorSelection(StatsActor.path) ? StatsActor.GetStats
    ).mapTo[Int]

    for {
      sunInfo     <- sunInfoF
      temperature <- temperatureF
      requests    <- requestsF
    } yield {
      Ok(views.html.index(sunInfo, temperature, requests))
    }
  }
}
