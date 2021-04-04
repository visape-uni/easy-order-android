package edu.uoc.easyorderfront.data.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import edu.uoc.easyorderfront.ui.utils.DataWrapper

class AuthenticationRepositoryImpl(
    private val authenticationBackendDataSource: AuthenticationBackendDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : AuthenticationRepository {

    private val TAG = "AuthenticationRepositoryImpl"

    override suspend fun login(email: String, password: String): MutableLiveData<DataWrapper<FirebaseUser?>> {
        val userMutableLiveData = firebaseDataSource.login(email, password)
        Log.d(TAG, "Login response $userMutableLiveData")

        return userMutableLiveData
    }

    override suspend fun register(username: String, email: String, password: String): User? {
        val registerResponse = authenticationBackendDataSource.register(username, email, password)

        Log.d(TAG, "Register response $registerResponse")
        return registerResponse
    }
}