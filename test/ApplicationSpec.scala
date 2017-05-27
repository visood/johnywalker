import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatest.concurrent._

import org.mockito.Mockito._
import play.api.libs.json.Json
import play.api.libs.ws.{WSResponse, WSRequest, WSClient}
import services.SunService

import scala.concurrent.Future

class ApplicationSpec extends PlaySpec with MockitoSugar with ScalaFutures
{
  "DateTimeFormat" must {
    "return 1070 as the beginning of epoch"  in {
      val beginning = new DateTime(0)
      val formattedYear = DateTimeFormat.forPattern("YYYY").print(beginning)
      formattedYear mustBe "1970"
    }
  }

  "SunService" must {
    "retrieve correct sunset and sunrise information" in {
      val wsClientStub = mock[WSClient]
      val wsRequestStub = mock[WSRequest]
      val wsResponseStub = mock[WSResponse]

      val expectedResponse = """
          {
            "results" : {
              "sunrise": "2016-04-14T05:18:12+01:00",
              "sunset" : "2016-04-15T16:31:52+01:00"
            }
          }
      """
      val jsResult = Json.parse(expectedResponse)
      val lat = 46.5197
      val lon = 6.6323
      val url = "http://api.sunrise-sunset.org/" +
        s"json?lat=$lat&lng=$lon&formatted=0"

      when(wsResponseStub.json).thenReturn(jsResult)
      when(wsRequestStub.get()).thenReturn(Future.successful(wsResponseStub))
      when(wsClientStub.url(url)).thenReturn(wsRequestStub)

      val sunService = new SunService(wsClientStub)
      val resultF    = sunService.getSunInfo(lat, lon)

      whenReady(resultF) { res =>
        println("sunrise time " + res.sunrise)
        println("sunset time " + res.sunset)
        res.sunrise mustBe "06:18:12"
        res.sunset  mustBe "17:31:52"
      }
      
    }
  }
}

