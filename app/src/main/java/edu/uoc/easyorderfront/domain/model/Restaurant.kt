package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.restaurant.RestaurantDTO

data class Restaurant(
        val id: String?,
        val name: String?,
        val street: String?,
        val city: String?,
        val zipCode: String?,
        val country: String?,
        val imageUrl: String? = null
) {
    fun convertToDTO(): RestaurantDTO {
        return RestaurantDTO(name, street, city, zipCode, country, imageUrl)
    }
}