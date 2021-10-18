package ch.cryptofinance.repositories

import ch.cryptofinance.models.{Order, OrderLimitType}
import com.google.inject.ImplementedBy
import scala.collection.mutable
import javax.inject.Singleton

@ImplementedBy(classOf[OrdersRepositoryImpl])
trait OrdersRepository {

  def create(order: Order): Order

  def find(id: String): Option[Order]

  def searchBuyOrders(status: String, minPriceLimit: Double): List[Order]

  def searchSellOrders(status: String, maxPriceLimit: Double): List[Order]

  def update(order: Order): Order

}

@Singleton
class OrdersRepositoryImpl extends OrdersRepository {

  val orders = mutable.Map[String, Order]()

  override def create(order: Order): Order = {
    orders.addOne(order.id -> order)
    order
  }

  override def find(id: String): Option[Order] = {
    orders.get(id)
  }

  override def searchBuyOrders(status: String, minPriceLimit: Double): List[Order] = {
    orders.values.filter(order =>
      order.status == status && order.`type` == OrderLimitType.BUY && order.priceLimit > minPriceLimit
    ).toList
  }

  override def searchSellOrders(status: String, maxPriceLimit: Double): List[Order] = {
    orders.values.filter(order =>
      order.status == status && order.`type` == OrderLimitType.SELL && order.priceLimit < maxPriceLimit
    ).toList
  }

  override def update(order: Order): Order = {
    orders(order.id) = order
    order
  }

}
