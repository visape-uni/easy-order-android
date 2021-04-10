package edu.uoc.easyorderfront.data.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import edu.uoc.easyorderfront.domain.model.User
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



    override suspend fun register(user: User): User? {
        val registerDTO = authenticationBackendDataSource.register(user.convertToDTO())

        Log.d(TAG, "Register response $registerDTO")
        return registerDTO?.convertToModel()
    }

    override suspend fun getIdToken(): MutableLiveData<DataWrapper<GetTokenResult>> {
        val tokenMutableLiveData = firebaseDataSource.getIdToken()
        Log.d(TAG, "getIdToken response $tokenMutableLiveData")

        return tokenMutableLiveData
    }
}