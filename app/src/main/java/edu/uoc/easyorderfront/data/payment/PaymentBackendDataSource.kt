package edu.uoc.easyorderfront.data.payment

import android.util.Log
import edu.uoc.easyorderfront.data.constants.Endpoints
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.order.OrderDTO
import io.ktor.client.*
import io.ktor.client.request.*

class PaymentBackendDataSource(private val httpClient: HttpClient) {
    private val TAG = "PaymentBackendDataSource"

    suspend fun fetchInitData(order: OrderDTO): PaymentResponseDTO {
        try {
            return httpClient
                .post<PaymentResponseDTO>(Endpoints.checkoutPaymentUrl) {
                    body = order
                }
        } catch(t: Throwable) {
            Log.w(TAG, "Error obteniendo payment", t)
            // TODO: TRATAR EXCEPCIONES
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }
}
