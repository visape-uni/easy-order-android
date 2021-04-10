package edu.uoc.easyorderfront.data.profile

import edu.uoc.easyorderfront.domain.model.User

interface ProfileRepository {
    suspend fun getProfile(id: String): User?
}