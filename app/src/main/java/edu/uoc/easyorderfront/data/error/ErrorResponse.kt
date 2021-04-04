package edu.uoc.easyorderfront.data.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorSerializer(
        val message: String
)