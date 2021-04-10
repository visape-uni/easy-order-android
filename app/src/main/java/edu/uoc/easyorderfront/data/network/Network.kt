package edu.uoc.easyorderfront.data.network

import android.content.Context
import android.util.Log
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
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
            }
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
                if (this.method != HttpMethod.Get) contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)

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

    private val json = kotlinx.serialization.json.Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }
}