package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.payment.PaymentResponseDTO

class PaymentResponse(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customer: String
) {
    fun convertToDTO(): PaymentResponseDTO {
        return PaymentResponseDTO(paymentIntent, ephemeralKey, customer)
    }
}
