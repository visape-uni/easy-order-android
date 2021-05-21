package edu.uoc.easyorderfront.data.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import edu.uoc.easyorderfront.domain.model.User

class AuthenticationRepositoryImpl(
    private val authenticationBackendDataSource: AuthenticationBackendDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : AuthenticationRepository {

    private val TAG = "AuthenticationRepositoryImpl"

    override suspend fun login(email: String, password: String): FirebaseUser? {
        val firebaseUserResponse = firebaseDataSource.login(email, password)
        Log.d(TAG, "Login response $firebaseUserResponse")

        return firebaseUserResponse
    }

    override suspend fun register(user: User): User? {
        val registerDTO = authenticationBackendDataSource.register(user.convertToDTO())

        Log.d(TAG, "Register response $registerDTO")
        return registerDTO?.convertToModel()
    }

    override suspend fun getIdToken(): String? {
        val token = firebaseDataSource.getToken()
        Log.d(TAG, "getIdToken response $token")

        return token
    }

    override suspend fun signOut() {
        firebaseDataSource.signOut()
    }
}