package edu.uoc.easyorderfront.data.profile

import android.util.Log
import edu.uoc.easyorderfront.data.authentication.UserDTO
import edu.uoc.easyorderfront.data.constants.Endpoints
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import io.ktor.client.*
import io.ktor.client.request.*

class ProfileBackendDataSource (private val httpClient: HttpClient) {
    private val TAG = "ProfileBackendDataSource"

    suspend fun getProfile(id: String): UserDTO? {
        try {
            return httpClient
                    .get<UserDTO>(Endpoints.getUserUrl + id)
        } catch(t: Throwable) {
            Log.w(TAG, "Error obteniendo perfil", t)
            // TODO: TRATAR EXCEPCIONES
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }
}