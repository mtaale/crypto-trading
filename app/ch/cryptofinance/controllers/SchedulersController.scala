package ch.cryptofinance.controllers

import akka.actor.ActorSystem
import ch.cryptofinance.services.OrdersService
import play.api.libs.ws.{WSClient, WSRequest}
import play.api.mvc._
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt


class SchedulersController @Inject()(
                                      val controllerComponents: ControllerComponents,
                                      val actorSystem: ActorSystem,
                                      val ws: WSClient,
                                      val ordersService: OrdersService
                                    ) extends BaseController {

  def startBtcPriceScheduler: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    actorSystem.scheduler.scheduleAtFixedRate(initialDelay = 0.seconds, interval = 1.seconds) { () =>
      val request: WSRequest = ws.url("http://localhost:5000/btc-price")
      request.get().map { response =>
        val btcPrice = (response.json \ "price").as[Double]
        println(s"price update: $btcPrice")
        ordersService.executeBuyOrders(btcPrice)
      }
    }
    Ok("Started...")
  }

}