import actors.StatsActor
import actors.StatsActor.Ping

import akka.actor.Props

import controllers.{Application, Assets}
import scala.concurrent.Future
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat

import filters.StatsFilter

import play.api.ApplicationLoader.Context
import play.api._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.Filter
import play.api.routing.Router

import router.Routes

import com.softwaremill.macwire._

import services.{SunService, WeatherService}

class AppApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    LoggerConfigurator(context.environment.classLoader).foreach { configurator =>
      configurator.configure(context.environment)
    }
    (new BuiltInComponentsFromContext(context) with AppComponents).application
  }

  trait AppComponents extends BuiltInComponents with AhcWSComponents {
    lazy val assets: Assets        = wire[Assets]
    lazy val prefix: String        = "/"
    lazy val router: Router        = wire[Routes]
    lazy val applicationController = wire[Application]

    lazy val sunService     = new SunService(wsClient)
    lazy val weatherService = new WeatherService(wsClient)

    lazy val statsFilter: Filter = wire[StatsFilter]
    override lazy val httpFilters = Seq(statsFilter)

    lazy val statsActor = actorSystem.actorOf(
      Props(wire[StatsActor]), StatsActor.name
    )


    def timeStamp(): String = {
      val now = new DateTime()
      val formatter = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss").
        withZone(DateTimeZone.forID("Europe/Zurich"))
      val timeStr: String = formatter.print(now)
      "<< " + timeStr + " >>"
    }

    applicationLifecycle.addStopHook { () =>
      Logger.info(timeStamp() + " The app is about to stop")
      Future.successful(Unit)
    }

    val onStart = {
      Logger.info(timeStamp() + ": The app is about to start")
      statsActor ! Ping
    }
  }
}
