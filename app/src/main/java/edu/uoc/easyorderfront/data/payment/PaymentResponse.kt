package edu.uoc.easyorderfront.data.payment

import edu.uoc.easyorderfront.domain.model.PaymentResponse
import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponseDTO(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customer: String
) {
    fun convertToModel(): PaymentResponse {
        return PaymentResponse(paymentIntent, ephemeralKey, customer)
    }
}