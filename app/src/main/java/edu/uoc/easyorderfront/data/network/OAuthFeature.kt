package edu.uoc.easyorderfront.data.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*

class OAuthFeature(
    private val getToken: suspend () -> String,
    private val refreshToken: suspend () -> Unit
) {
    class Config {
        lateinit var getToken: suspend () -> String
        lateinit var refreshToken: suspend () -> Unit
    }

    companion object Feature : HttpClientFeature<Config, OAuthFeature> {
        private val TAG = "MyRequestInterceptor"
        override val key: AttributeKey<OAuthFeature> = AttributeKey("OAuth")

        override fun prepare(block: Config.() -> Unit): OAuthFeature {
            val config = Config().apply(block)
            return OAuthFeature(config.getToken, config.refreshToken)
        }

        private val RefreshKey = "Ktor-OAuth-Refresh"

        override fun install(feature: OAuthFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                context.headers[RefreshKey] = context.headers.contains("Authorization").toString()

                val token = feature.getToken()
                if (token.isNotBlank()) {
                    context.headers["Authorization"] = "Bearer ${token}"
                }

                proceed()
            }

            // Intercept response
            scope.receivePipeline.intercept(HttpReceivePipeline.After) {
                if (subject.status == HttpStatusCode.Unauthorized
                        && context.request.headers[RefreshKey] != true.toString()) {
                    try {
                        // Execute RefreshToken
                        feature.refreshToken()

                        // Retry request
                        val call = scope.requestPipeline.execute(
                            HttpRequestBuilder().takeFrom(context.request),
                            context.request.content
                        ) as HttpClientCall

                        proceedWith(call.response)

                        return@intercept
                    } catch (exception: Exception) {
                        // If refresh fails, proceed as 401
                    }
                } else if (subject.status == HttpStatusCode.RequestTimeout) {
                    // Retry request
                    val call = scope.requestPipeline.execute(
                            HttpRequestBuilder().takeFrom(context.request),
                            context.request.content
                    ) as HttpClientCall

                    proceedWith(call.response)

                    return@intercept
                }
                // Proceed as normal request
                proceedWith(subject)
            }
        }
    }
}