package edu.uoc.easyorderfront.data.restaurant

import edu.uoc.easyorderfront.data.authentication.UserDTO
import edu.uoc.easyorderfront.domain.model.Restaurant
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantDTO(
        val uid: String?,
        val name: String? = null,
        val street: String? = null,
        val city: String? = null,
        val zipCode: String? = null,
        val country: String? = null,
        val imageUrl: String? = null,
        val workers: List<UserDTO>? = null,
        val owner: UserDTO? = null
) {
    fun convertToModel(): Restaurant {
        val workersList = workers?.map { userDTO -> userDTO.convertToModel() }
        return Restaurant(uid, name, street, city, zipCode, country, imageUrl, workersList, owner?.convertToModel())
    }
}