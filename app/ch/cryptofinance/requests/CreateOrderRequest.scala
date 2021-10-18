package ch.cryptofinance.requests

import ch.cryptofinance.models.Order
import play.api.libs.json.{Json, OFormat}

case class CreateOrderRequest(
                             accountId: String,
                             `type`: String,
                             priceLimit: Double,
                             amount: Double
                             )


object CreateOrderRequest {

  implicit val format: OFormat[CreateOrderRequest] = Json.format[CreateOrderRequest]

}
