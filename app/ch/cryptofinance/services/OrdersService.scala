package ch.cryptofinance.services

import ch.cryptofinance.models.{AccountType, Order, OrderStatus}
import ch.cryptofinance.repositories.OrdersRepository
import ch.cryptofinance.requests.CreateOrderRequest
import com.google.inject.ImplementedBy
import java.util.UUID
import javax.inject.{Inject, Singleton}

@ImplementedBy(classOf[OrdersServiceImpl])
trait OrdersService {

  def create(request: CreateOrderRequest): Option[Order]

  def find(id: String): Option[Order]

  def executeSellOrders(btcPrice: Double): List[Order]

  def executeBuyOrders(btcPrice: Double): List[Order]

}

@Singleton
class OrdersServiceImpl @Inject()(repository: OrdersRepository, accountsService: AccountsService) extends OrdersService {

  override def create(request: CreateOrderRequest): Option[Order] = {
    val account = accountsService.find(request.accountId)
    if (account.isEmpty) {
      return None
    }
    val order = Order(
      UUID.randomUUID().toString,
      request.accountId,
      request.`type`,
      OrderStatus.PENDING,
      request.priceLimit,
      request.amount,
      System.currentTimeMillis(),
      System.currentTimeMillis()
    )
    Some(repository.create(order))
  }

  override def find(id: String): Option[Order] = {
    repository.find(id)
  }

  override def executeBuyOrders(btcPrice: Double): List[Order] = {
    val pendingOrders = repository.searchBuyOrders(OrderStatus.PENDING, btcPrice)
    for {
      order <- pendingOrders
    } yield {
      val account = accountsService.find(order.accountId)
        .getOrElse(throw new RuntimeException(s"cannot find account ${order.accountId}"))

      if (account.hasSufficientBalance(AccountType.USD, order.amount)) {
        // transfer amounts for usd and btc accounts
        val btcAmount = order.amount / btcPrice
        accountsService.debit(account.id, AccountType.USD, order.amount)
        accountsService.credit(account.id, AccountType.BTC, btcAmount)

        // update the order status
        val updateOrder = order.copy(status = OrderStatus.PROCESSED, updatedAt = System.currentTimeMillis())
        repository.update(updateOrder)

        println(s"order executed: $updateOrder")
      }
      else {
        println(s"insufficient account balance to execute order ${order.id}")
      }
      order
    }


  }

  override def executeSellOrders(btcPrice: Double): List[Order] = ???

}
