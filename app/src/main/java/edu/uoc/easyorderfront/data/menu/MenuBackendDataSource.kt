package edu.uoc.easyorderfront.data.menu

import android.util.Log
import edu.uoc.easyorderfront.data.constants.Endpoints
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import io.ktor.client.*
import io.ktor.client.request.*

class MenuBackendDataSource(private val httpClient: HttpClient) {
    private val TAG = "MenuBackendDataSource"

    suspend fun getMenu(restaurantId: String): MenuDTO? {
        try {
            return httpClient
                .get<MenuDTO>(Endpoints.getMenuUrl + restaurantId)
        } catch (t: Throwable) {
            Log.w(TAG, "Error obteniendo menu", t)
            // TODO: TRATAR EXCEPCIONES
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }
}