package edu.uoc.easyorderfront.data.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_CREDENCIALES_INCORRECTA
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_GENERICO
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_USUARIO_INEXISTENTE
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait

class FirebaseDataSource(
    private val auth: FirebaseAuth = Firebase.auth
) {

    private val TAG = "FirebaseDataSource"

    suspend fun login(email: String, password: String): MutableLiveData<DataWrapper<FirebaseUser?>> {

        val userMutableLiveData = MutableLiveData<DataWrapper<FirebaseUser?>>(DataWrapper.loading(null));
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { task ->
            Log.d(TAG, "SignInWithEmail:success")
            val user = auth.currentUser
            userMutableLiveData.postValue(DataWrapper.success(user))
        }.addOnFailureListener {exception ->
            Log.w(TAG, "SignInWithEmail:failure", exception)
            val message : String
            when (exception) {
                is FirebaseAuthInvalidCredentialsException -> {
                    message = ERROR_CREDENCIALES_INCORRECTA
                }
                is FirebaseAuthInvalidUserException -> {
                    message = ERROR_USUARIO_INEXISTENTE
                }
                else -> {
                    message = ERROR_GENERICO
                }
            }

            userMutableLiveData.postValue(DataWrapper.error(message))

        }

        return userMutableLiveData
    }

    suspend fun getIdToken():MutableLiveData<DataWrapper<GetTokenResult>> {
        val tokenMutableLiveData = MutableLiveData<DataWrapper<GetTokenResult>>(DataWrapper.loading(null))

        val currentUser = auth.currentUser
        currentUser.getIdToken(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "GetToken:success")
                val tokenResult = task.getResult()
                tokenMutableLiveData.postValue(DataWrapper.success(tokenResult))
            } else {
                val exception = task.exception
                Log.w(TAG, "GetToken:failure", exception)
                if (exception?.message != null) {
                    tokenMutableLiveData.postValue(DataWrapper.error(exception.message!!))
                } else {
                    tokenMutableLiveData.postValue(DataWrapper.error(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION))
                }
            }
        }
        return tokenMutableLiveData
    }


    suspend fun getRefreshToken(): String? {
        val currentUser = auth.currentUser
        var token: String? = null
        if (currentUser != null) {
            Log.d(TAG, "RefreshToken STARTS")
            runBlocking {
                val result = async {
                    currentUser.getIdToken(true).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            token = task.result?.token
                            Log.d(TAG, "RefreshToken Token: $token")
                        }
                    }
                }.wait()
                Log.d(TAG, "RefreshToken Wait END")
            }
        }
        return token
    }
}