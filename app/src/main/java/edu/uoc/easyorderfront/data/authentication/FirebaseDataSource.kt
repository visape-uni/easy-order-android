package edu.uoc.easyorderfront.data.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_CREDENCIALES_INCORRECTA
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_GENERICO
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_USUARIO_INEXISTENTE
import kotlinx.coroutines.tasks.await

class FirebaseDataSource(
    private val auth: FirebaseAuth = Firebase.auth
) {

    private val TAG = "FirebaseDataSource"

    suspend fun login(email: String, password: String): FirebaseUser? {
        Log.d(TAG, "SignInWithEmail")
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = auth.currentUser
            Log.d(TAG, "SignInWithEmail:success")
            return user
        } catch(e: Exception) {
            Log.w(TAG, "SignInWithEmail:failure", e)
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    throw EasyOrderException(ERROR_CREDENCIALES_INCORRECTA)
                }
                is FirebaseAuthInvalidUserException -> {
                    throw EasyOrderException(ERROR_USUARIO_INEXISTENTE)
                }
                else -> {
                    throw EasyOrderException(ERROR_GENERICO)
                }
            }
        }
    }

    suspend fun getToken(): String? {
        Log.d(TAG, "RefreshToken STARTS")
        val currentUser = auth.currentUser
        var token: String? = null
        if ( currentUser != null) {
            val tokenResult = currentUser.getIdToken(true).await()

            token = tokenResult.token
            Log.d(TAG, "RefreshToken Token: $token")
        }
        Log.d(TAG, "RefreshToken END")
        return token
    }

    suspend fun signOut() {
        Log.d(TAG, "SignOut")
        auth.signOut()
    }
}