package edu.uoc.easyorderfront.data.authentication

import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.User
import edu.uoc.easyorderfront.domain.model.Worker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// UserDTO
@Serializable
data class UserDTO(
        @SerialName("uid") val uid:String?,
        @SerialName("username") val username:String?,
        @SerialName("email") val email:String?,
        @SerialName("password") val password:String? = null,
        @SerialName("isClient") val isClient:Boolean? = null,
        @SerialName("idRestaurant") val restaurant:String? = null

) {
    fun convertToModel(): User {
        val user: User
        if (isClient != null && isClient) {
            user = User(uid, username, email, password, isClient)
        } else {
            user = Worker(uid, username, email, password, Restaurant(restaurant))
        }
        return user
    }
}


