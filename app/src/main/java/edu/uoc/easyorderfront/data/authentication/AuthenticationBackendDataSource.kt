package edu.uoc.easyorderfront.data.authentication

import android.util.Log
import edu.uoc.easyorderfront.data.constants.Endpoints
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.ErrorSerializer
import edu.uoc.easyorderfront.data.error.MyException
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.charset.Charset

class AuthenticationBackendDataSource(private val httpClient: HttpClient) {
    private val TAG = "AuthenticationBackendDataSource"

    suspend fun register(username: String, email: String, password: String, isClient:Boolean): User? {
        try {
            return httpClient
                    .post<User>(Endpoints.registerUrl) {
                        body = User(null, username, email, password, isClient);
                    }
        } catch (t: Throwable) {

            Log.w(TAG, "Error creando usuario",t)

            when (t) {
                is ClientRequestException -> {
                    // Usuario ya existe, badrequest, unauthorized...
                    val errorString = t.response?.readText(Charset.defaultCharset())
                    val errorResponse = errorString?.let { Json.decodeFromString<ErrorSerializer>(it) }
                    Log.w(TAG, "${errorResponse?.message}")

                    if(errorResponse?.message != null) {
                        throw MyException(errorResponse.message)
                    }
                }

                is ServerResponseException -> {
                    // Internal server error (backend error)
                    val errorString = t.response?.readText(Charset.defaultCharset())
                    val errorResponse = errorString?.let { Json.decodeFromString<ErrorSerializer>(it) }
                    Log.w(TAG, "${errorResponse?.message}")

                    if(errorResponse?.message != null) {
                        throw MyException(errorResponse.message)
                    }
                }
            }

            throw MyException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }
}