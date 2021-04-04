package edu.uoc.easyorderfront.data.authentication

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import edu.uoc.easyorderfront.ui.utils.DataWrapper

interface AuthenticationRepository {

    // Devuelve true si esta logeado, false si no
    suspend fun login(email: String, password: String): MutableLiveData<DataWrapper<FirebaseUser?>>

    // Devuelve true si se registra correctamente, false si no
    suspend fun register(username: String, email: String, password: String): User?
}