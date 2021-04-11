package edu.uoc.easyorderfront.data.restaurant

import edu.uoc.easyorderfront.domain.model.Restaurant
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantDTO(
        val name: String?,
        val street: String?,
        val city: String?,
        val zipCode: String?,
        val country: String?,
        val imageUrl: String? = null
) {
    fun convertToModel(): Restaurant {
        return Restaurant(name, street, city, zipCode, country, imageUrl)
    }
}