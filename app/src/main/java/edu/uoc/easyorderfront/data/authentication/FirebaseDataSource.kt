package edu.uoc.easyorderfront.data.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_CREDENCIALES_INCORRECTA
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_GENERICO
import edu.uoc.easyorderfront.ui.constants.UIMessages.ERROR_USUARIO_INEXISTENTE
import edu.uoc.easyorderfront.ui.utils.DataWrapper

class FirebaseDataSource(private val auth: FirebaseAuth = Firebase.auth) {

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

}