package edu.uoc.easyorderfront.data.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
        @SerialName("uid") val uid:String?,
        @SerialName("username") val username:String?,
        @SerialName("email") val email:String?,
        @SerialName("password") val password:String? = null,
        @SerialName("isClient") val isClient:Boolean? = null
)

@Serializable
data class AuthenticationResponse(
    @SerialName("access_token") val accessToken: String
)

