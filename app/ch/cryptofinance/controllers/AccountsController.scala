package ch.cryptofinance.controllers

import ch.cryptofinance.models.Account
import ch.cryptofinance.requests.CreateAccountRequest
import ch.cryptofinance.services.AccountsService
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.{Inject, Singleton}

@Singleton
class AccountsController @Inject()(val controllerComponents: ControllerComponents, val accountsService: AccountsService) extends BaseController {

  def create: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val createRequest = request.body.asJson.get.as[CreateAccountRequest]
    val account = accountsService.create(createRequest)
    Ok(Json.toJson[Account](account))
  }

  def find(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val account = accountsService.find(id)
    if (account.nonEmpty) {
      Ok(Json.toJson[Account](account.get))
    }
    else NotFound
  }

}
