package edu.uoc.easyorderfront.data.authentication

import edu.uoc.easyorderfront.data.restaurant.RestaurantDTO
import edu.uoc.easyorderfront.domain.model.User
import edu.uoc.easyorderfront.domain.model.Worker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// UserDTO
@Serializable
data class UserDTO(
        @SerialName("uid") val uid:String?,
        @SerialName("username") val username:String? = null,
        @SerialName("email") val email:String? = null,
        @SerialName("password") val password:String? = null,
        @SerialName("isClient") val isClient:Boolean? = null,
        @SerialName("isOwner") val isOwner:Boolean? = null,
        @SerialName("tableId") val tableId:String? = null,
        @SerialName("restaurant") val restaurant:RestaurantDTO? = null
) {
    fun convertToModel(): User {
        val user: User
        if (isClient != null && isClient) {
            user = User(uid, username, email, password, isClient, tableId)
        } else {
            user = Worker(uid, username, email, password, isOwner, restaurant?.convertToModel())
        }
        return user
    }
}


