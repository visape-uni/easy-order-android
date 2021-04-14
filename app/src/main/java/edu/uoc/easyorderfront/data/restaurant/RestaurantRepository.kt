package edu.uoc.easyorderfront.data.restaurant

import edu.uoc.easyorderfront.domain.model.Restaurant

interface RestaurantRepository {

    suspend fun createRestaurant(restaurant: Restaurant): Restaurant?
    suspend fun getRestaurant(id: String): Restaurant?

}