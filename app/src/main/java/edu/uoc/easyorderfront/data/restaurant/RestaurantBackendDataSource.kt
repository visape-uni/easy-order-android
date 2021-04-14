package edu.uoc.easyorderfront.data.restaurant

import android.util.Log
import edu.uoc.easyorderfront.data.constants.Endpoints
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.domain.model.Restaurant
import io.ktor.client.*
import io.ktor.client.request.*

class RestaurantBackendDataSource(private val httpClient: HttpClient) {
    private val TAG = "RestaurantBackendDataSource"

    suspend fun createRestaurant(restaurantDTO: RestaurantDTO): RestaurantDTO? {
        try {
            return httpClient
                    .post<RestaurantDTO>(Endpoints.createRestaurantUrl) {
                        body = restaurantDTO
                    }
        } catch (t: Throwable) {
            Log.w(TAG, "Error creando restaurante", t)
            //TODO: TRATAR EXCEPCIONES
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }

    suspend fun getRestaurant(id: String): RestaurantDTO? {
        TODO("Not yet implemented")
    }
}