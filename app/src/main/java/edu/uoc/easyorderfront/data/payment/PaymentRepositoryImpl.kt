package edu.uoc.easyorderfront.data.payment

import android.util.Log
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.domain.model.PaymentResponse

class PaymentRepositoryImpl(
    private val paymentBackendDataSource: PaymentBackendDataSource
): PaymentRepository {
    private val TAG = "PaymentRepositoryImpl"

    override suspend fun fetchInitData(order: Order): PaymentResponse? {
        val paymentResponseDTO = paymentBackendDataSource.fetchInitData(order.converToDTO())
        Log.d(TAG, "FetchInitData response $paymentResponseDTO")

        return paymentResponseDTO?.convertToModel()
    }
}