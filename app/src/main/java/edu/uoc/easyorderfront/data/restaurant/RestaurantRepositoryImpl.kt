package edu.uoc.easyorderfront.data.restaurant

import android.util.Log
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Worker

class RestaurantRepositoryImpl(
        private val restaurantBackendDataSource: RestaurantBackendDataSource
): RestaurantRepository {

    private val TAG = "RestaurantRepositoryImpl"

    override suspend fun createRestaurant(restaurant: Restaurant): Restaurant? {
        val restaurantDTO = restaurantBackendDataSource.createRestaurant(restaurant.convertToDTO())

        Log.d(TAG, "CreateRestaurant response $restaurantDTO")

        return restaurantDTO?.convertToModel()
    }

    override suspend fun getRestaurant(id: String): Restaurant? {
        val restaurantDTO = restaurantBackendDataSource.getRestaurant(id)

        Log.d(TAG, "GetRestaurant response $restaurantDTO")

        return restaurantDTO?.convertToModel()
    }

    override suspend fun addWorker(restaurantId: String, workerId: String): Worker? {
        val workerDTO = restaurantBackendDataSource.addWorker(restaurantId, workerId)

        Log.d(TAG, "AddWorker response $workerDTO")

        return workerDTO?.convertToModel() as Worker
    }

    override suspend fun removeWorker(restaurantId: String, workerId: String): Boolean {
        return restaurantBackendDataSource.removeWorker(restaurantId, workerId)
    }
}