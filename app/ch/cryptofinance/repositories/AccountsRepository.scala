package ch.cryptofinance.repositories

import ch.cryptofinance.models.Account
import com.google.inject.ImplementedBy
import javax.inject.Singleton
import scala.collection.mutable

@ImplementedBy(classOf[AccountsRepositoryImpl])
trait AccountsRepository {

  def create(account: Account): Account

  def find(id: String): Option[Account]

  def update(account: Account): Account

}

@Singleton
class AccountsRepositoryImpl extends AccountsRepository {

  val accounts = mutable.Map[String, Account]()

  override def create(account: Account): Account = {
    accounts.addOne(account.id -> account)
    account
  }

  override def find(id: String): Option[Account] = {
    accounts.get(id)
  }

  override def update(account: Account): Account = {
    accounts(account.id) = account
    account
  }

}
