package edu.uoc.easyorderfront.data.profile

import android.util.Log
import edu.uoc.easyorderfront.data.authentication.AuthenticationBackendDataSource
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.domain.model.User

class ProfileRepositoryImpl(private val profileBackendDataSource: ProfileBackendDataSource)
    : ProfileRepository {

    private val TAG = "ProfileRepositoryImpl"

    override suspend fun getProfile(id: String): User? {
        val profileDTO = profileBackendDataSource.getProfile(id)
        Log.d(TAG, "Get Profile response $profileDTO")

        return profileDTO?.convertToModel()
    }
}