package ch.cryptofinance.services

import ch.cryptofinance.models.{Account, AccountType}
import ch.cryptofinance.repositories.AccountsRepository
import ch.cryptofinance.requests.CreateAccountRequest
import com.google.inject.ImplementedBy
import java.util.UUID
import javax.inject.{Inject, Singleton}

@ImplementedBy(classOf[AccountsServiceImpl])
trait AccountsService {

  def create(request: CreateAccountRequest): Account

  def find(id: String): Option[Account]

  def credit(id: String, accountType: String, amount: Double): Account

  def debit(id: String, accountType: String, amount: Double): Account

}

@Singleton
class AccountsServiceImpl @Inject()(repository: AccountsRepository) extends AccountsService {

  override def create(request: CreateAccountRequest): Account = {
    val account = Account(
      UUID.randomUUID().toString,
      request.name,
      request.usdBalance,
      0,
      System.currentTimeMillis(),
      System.currentTimeMillis()
    )

    repository.create(account)
  }

  override def find(id: String): Option[Account] = {
    repository.find(id)
  }

  override def credit(id: String, accountType: String, amount: Double): Account = {
    val account = find(id)
      .getOrElse(throw new RuntimeException(s"cannot find account with $id"))

    val updateAccount =
      accountType match {
        case AccountType.BTC => account.copy(btcBalance = account.btcBalance + amount)
        case AccountType.USD => account.copy(usdBalance = account.usdBalance + amount)
        case _ => throw new RuntimeException(s"cannot identify the account type $accountType")
      }

    repository.update(updateAccount)
  }

  override def debit(id: String, accountType: String, amount: Double): Account = {
    val account = find(id)
      .getOrElse(throw new RuntimeException(s"cannot find account with $id"))

    if (!account.hasSufficientBalance(accountType, amount)) {
      throw new RuntimeException(s"insufficient account $accountType balance for account $id")
    }

    val updateAccount =
      accountType match {
        case AccountType.BTC =>
          account.copy(btcBalance = account.btcBalance - amount, updatedAt = System.currentTimeMillis())
        case AccountType.USD =>
          account.copy(usdBalance = account.usdBalance - amount, updatedAt = System.currentTimeMillis())
        case _ => throw new RuntimeException(s"cannot identify the account type $accountType")
      }

    repository.update(updateAccount)
  }

}
