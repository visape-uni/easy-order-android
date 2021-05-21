package edu.uoc.easyorderfront.data.payment

import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.domain.model.PaymentResponse

interface PaymentRepository {
    suspend fun fetchInitData(order: Order): PaymentResponse?
}