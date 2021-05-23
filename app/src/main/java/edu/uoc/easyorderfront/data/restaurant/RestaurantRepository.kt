package edu.uoc.easyorderfront.data.restaurant

import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Worker

interface RestaurantRepository {

    suspend fun createRestaurant(restaurant: Restaurant): Restaurant?
    suspend fun getRestaurant(id: String): Restaurant?
    suspend fun addWorker(restaurantId: String, workerId: String): Worker?
    suspend fun removeWorker(restaurantId: String, workerId: String): Boolean

}