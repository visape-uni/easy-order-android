package edu.uoc.easyorderfront.data.order

import android.util.Log
import edu.uoc.easyorderfront.data.constants.Endpoints
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import io.ktor.client.*
import io.ktor.client.request.*

class OrderBackendDataSource(private val httpClient: HttpClient) {
    private val TAG = "OrderBackendDataSource"

    suspend fun getLastOrder(tableId: String): OrderDTO? {
        try {
            return httpClient
                    .get<OrderDTO>(Endpoints.getLastOrderUrl + tableId)
        } catch (t: Throwable) {
            Log.w(TAG, "Error obteniendo último pedido", t)
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }

    suspend fun saveOrder(tableId: String, order: OrderDTO): OrderDTO? {
        try {
            return httpClient
                    .post<OrderDTO>(Endpoints.saveOrderUrl + tableId) {
                        body = order
                    }
        } catch (t: Throwable) {
            Log.w(TAG, "Error guardando pedido", t)
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }
}