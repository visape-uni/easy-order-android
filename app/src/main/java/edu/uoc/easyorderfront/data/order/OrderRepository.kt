package edu.uoc.easyorderfront.data.order

import edu.uoc.easyorderfront.domain.model.Order

interface OrderRepository {
    suspend fun getLastOrder(tableId: String): Order?
    suspend fun saveOrder(tableId: String, order: Order): Order?
}