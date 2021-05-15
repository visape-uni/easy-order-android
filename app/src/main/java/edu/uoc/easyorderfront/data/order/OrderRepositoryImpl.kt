package edu.uoc.easyorderfront.data.order

import android.util.Log
import edu.uoc.easyorderfront.domain.model.Order

class OrderRepositoryImpl(
        private val orderBackEndDataSource: OrderBackendDataSource
): OrderRepository {
    private val TAG = "OrderRepositoryImpl"

    override suspend fun getLastOrder(tableId: String): Order? {
        val orderDTO = orderBackEndDataSource.getLastOrder(tableId)

        Log.d(TAG, "Get last order response $orderDTO")

        return orderDTO?.convertToModel()
    }

    override suspend fun saveOrder(tableId: String, order: Order): Order? {
        val orderDTO = orderBackEndDataSource.saveOrder(tableId, order.converToDTO())

        Log.d(TAG, "Save order respone $orderDTO")

        return orderDTO?.convertToModel()
    }
}