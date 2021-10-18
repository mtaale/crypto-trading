package ch.cryptofinance.requests

import play.api.libs.json.{Json, OFormat}

case class CreateAccountRequest(
                               name: String,
                               usdBalance: Double
                               )

object CreateAccountRequest {

  implicit val format: OFormat[CreateAccountRequest] = Json.format[CreateAccountRequest]

}
