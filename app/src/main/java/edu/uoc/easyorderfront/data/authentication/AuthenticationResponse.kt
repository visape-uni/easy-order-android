package edu.uoc.easyorderfront.data.authentication

import edu.uoc.easyorderfront.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// UserDTO
@Serializable
data class UserDTO(
        @SerialName("uid") val uid:String?,
        @SerialName("username") val username:String?,
        @SerialName("email") val email:String?,
        @SerialName("password") val password:String? = null,
        @SerialName("isClient") val isClient:Boolean? = null
) {
    fun convertToModel(): User {
        return User(uid, username, email, password, isClient)
    }
}


