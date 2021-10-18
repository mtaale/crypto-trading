package ch.cryptofinance.models

import play.api.libs.json.{Json, OFormat}

case class Order(
                id: String,
                accountId: String,
                `type`: String,
                status: String,
                priceLimit: Double,
                amount: Double,
                createdAt: Long,
                updatedAt: Long
                )

object Order {

  implicit val format: OFormat[Order] = Json.format[Order]

}

object OrderLimitType extends Enumeration {
  val BUY = "buy"
  val SELL = "sell"
}

object OrderStatus extends Enumeration {
  val PENDING = "pending"
  val PROCESSED = "processed"
}