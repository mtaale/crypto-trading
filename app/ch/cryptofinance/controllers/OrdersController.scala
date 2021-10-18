package ch.cryptofinance.controllers

import ch.cryptofinance.models.Order
import ch.cryptofinance.requests.CreateOrderRequest
import ch.cryptofinance.services.OrdersService
import play.api.libs.json.Json
import play.api.mvc._
import javax.inject.{Inject, Singleton}

@Singleton
class OrdersController @Inject()(val controllerComponents: ControllerComponents, val ordersService: OrdersService) extends BaseController {

  def create: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val createRequest = request.body.asJson.get.as[CreateOrderRequest]
    val order = ordersService.create(createRequest)
    if (order.nonEmpty) {
      Ok(Json.toJson[Order](order.get))
    }
    else Ok(s"account id ${createRequest.accountId} not found")
  }

  def find(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val order = ordersService.find(id)
    if (order.nonEmpty) {
      Ok(Json.toJson[Order](order.get))
    }
    else NotFound
  }

}

