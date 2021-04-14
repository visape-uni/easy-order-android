package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.restaurant.RestaurantDTO

data class Restaurant(
        val id: String?,
        val name: String? = null,
        val street: String? = null,
        val city: String? = null,
        val zipCode: String? = null,
        val country: String? = null,
        val imageUrl: String? = null,
        val workers: List<User>? = null,
        val owner: User? = null
) {
    fun convertToDTO(): RestaurantDTO {
        val workersList = workers?.map { user -> user.convertToDTO() }
        return RestaurantDTO(id, name, street, city, zipCode, country, imageUrl, workersList, owner?.convertToDTO())
    }
}