package ch.cryptofinance.models

import play.api.libs.json.{Json, OFormat}

case class Account(
                  id: String,
                  name: String,
                  usdBalance: Double,
                  btcBalance: Double,
                  createdAt: Long,
                  updatedAt: Long
                  ) {

  def hasSufficientBalance(accountType: String, amount: Double): Boolean = {
    accountType match {
      case AccountType.USD => usdBalance >= amount
      case AccountType.BTC => btcBalance >= amount
    }
  }
}

object Account {

  implicit val format: OFormat[Account] = Json.format[Account]

}

object AccountType extends Enumeration {
  val USD = "usd"
  val BTC = "btc"
}
