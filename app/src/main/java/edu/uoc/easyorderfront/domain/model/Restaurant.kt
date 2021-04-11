package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.restaurant.RestaurantDTO

data class Restaurant(
        val id: String?,
        val name: String? = null,
        val street: String? = null,
        val city: String? = null,
        val zipCode: String? = null,
        val country: String? = null,
        val imageUrl: String? = null
) {
    fun convertToDTO(): RestaurantDTO {
        return RestaurantDTO(name, street, city, zipCode, country, imageUrl)
    }
}