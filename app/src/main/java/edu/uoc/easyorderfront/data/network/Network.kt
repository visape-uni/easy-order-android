package edu.uoc.easyorderfront.data.network

import android.content.Context
import android.util.Log
import edu.uoc.easyorderfront.data.SessionManager
import edu.uoc.easyorderfront.data.authentication.FirebaseDataSource
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

object Network {
    private const val TAG = "Network"

    fun createHttpClient(context: Context): HttpClient {
        return HttpClient(OkHttp) {
            // Json
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            // Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v(TAG, message)
                    }
                }
                level = LogLevel.ALL
            }
            // Timeout
            install(HttpTimeout) {
                requestTimeoutMillis = 60000L
                connectTimeoutMillis = 60000L
                socketTimeoutMillis = 60000L
            }
            // Apply to All Requests
            defaultRequest {
                // Content Type
                if (this.method != HttpMethod.Get) {
                    contentType(ContentType.Application.Json)
                }
                accept(ContentType.Application.Json)

                // TODO: PONER TOKEN
                /* (checkUrlToken(this.url.encodedPath)) {
                    header("Authorization", "Bearer " + SessionManager(context).getAccessToken())
                }*/

            }

            HttpResponseValidator {
                validateResponse { response ->
                    val statusCode = response.status.value
                    when (statusCode) {
                        in 300..399 -> throw RedirectResponseException(response)
                        in 400..499 -> throw ClientRequestException(response)
                        in 500..599 -> throw ServerResponseException(response)
                    }
                }
            }

            install(OAuthFeature) {
                getToken = {
                    val accessToken = SessionManager(context).getAccessToken() ?: ""

                    Log.d(TAG, "Adding Bearer header with token $accessToken")
                    accessToken
                }
                refreshToken = {
                    SessionManager(context).clearAccessToken()
                    launchTokenRefresh(context)
                }
            }


            // Add OAuth Feature
            /*install(OAuthFeature) {
                getToken = {
                    val accessToken = SessionManager(context).getAccessToken() ?: ""

                    Log.d(TAG, "Adding Bearer header with token $accessToken")
                    accessToken
                }
                refreshToken = {
                    // Remove expired access token
                    SessionManager(context).clearAccessToken()
                    // Launch token refresh request
                    launchTokenRefresh(context)
                }
            }*/
        }
    }

    private suspend fun launchTokenRefresh(context: Context) {
        Log.d(TAG, "LaunchTokenRefresh:Start")

        val token = FirebaseDataSource().getToken()
        Log.d(TAG, "LaunchTokenRefresh:token $token")
        token?.let { token ->
            SessionManager(context).saveAccessToken(token)
        }
        Log.d(TAG, "LaunchTokenRefresh: END")
    }

    private val json = kotlinx.serialization.json.Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    private val API_ENDPOINT = "/api"
    private val GET_USER_ENDPOINT = "/user/get"

    private fun checkUrlToken(url: String): Boolean {
        return (url.contains(API_ENDPOINT) || url.contains(GET_USER_ENDPOINT))
    }
}