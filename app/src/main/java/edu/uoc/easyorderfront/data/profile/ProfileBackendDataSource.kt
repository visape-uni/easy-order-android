package edu.uoc.easyorderfront.data.profile

import edu.uoc.easyorderfront.data.authentication.UserDTO
import io.ktor.client.*

class ProfileBackendDataSource (private val httpClient: HttpClient) {
    private val TAG = "ProfileBackendDataSource"

    suspend fun getProfile(id: String): UserDTO? {

    }
}