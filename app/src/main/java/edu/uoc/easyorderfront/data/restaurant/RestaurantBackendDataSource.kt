package edu.uoc.easyorderfront.data.restaurant

import android.util.Log
import edu.uoc.easyorderfront.data.authentication.UserDTO
import edu.uoc.easyorderfront.data.constants.Endpoints
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.error.ErrorDTO
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.charset.Charset

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
        try {
            return httpClient
                .get<RestaurantDTO>(Endpoints.getRestaurantUrl + id)
        } catch(t: Throwable) {
            Log.w(TAG, "Error obteniendo restaurante", t)
            // TODO: TRATAR EXCEPCIONES
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }

    suspend fun addWorker(restaurantId: String, workerId: String): UserDTO? {
        try {
            return httpClient
                    .put<UserDTO>(Endpoints.addWorkerUrl + restaurantId + "/" + workerId)
        } catch(t: Throwable) {
            Log.w(TAG, "Error añadiendo trabajador", t)

            when (t) {
                is ClientRequestException -> {
                    val errorString = t.response?.readText(Charset.defaultCharset())
                    val errorResponse = errorString?.let { Json.decodeFromString<ErrorDTO>(it) }
                    Log.w(TAG, "${errorResponse?.message}")

                    if (errorResponse?.message != null) {
                        throw EasyOrderException(errorResponse.message)
                    }
                }

            }
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }

    suspend fun removeWorker(restaurantId: String, workerId: String): Boolean {
        try {
            return httpClient
                    .put<Boolean>(Endpoints.removeWorkerUrl + restaurantId + "/" + workerId)
        } catch(t: Throwable) {
            Log.w(TAG, "Error añadiendo trabajador", t)

            when (t) {
                is ClientRequestException -> {
                    val errorString = t.response?.readText(Charset.defaultCharset())
                    val errorResponse = errorString?.let { Json.decodeFromString<ErrorDTO>(it) }
                    Log.w(TAG, "${errorResponse?.message}")

                    if (errorResponse?.message != null) {
                        throw EasyOrderException(errorResponse.message)
                    }
                }

            }
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }
}