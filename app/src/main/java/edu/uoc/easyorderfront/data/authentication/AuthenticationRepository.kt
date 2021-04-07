package edu.uoc.easyorderfront.data.authentication

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import edu.uoc.easyorderfront.ui.utils.DataWrapper

interface AuthenticationRepository {

    // Devuelve el usuario de firebase o una excepcion
    suspend fun login(email: String, password: String): MutableLiveData<DataWrapper<FirebaseUser?>>

    // Devuelve el usuario registrado o una excepcion
    suspend fun register(username: String, email: String, password: String, isClient:Boolean): User?

    // Devuelve el token del usuario actual
    suspend fun getIdToken(): MutableLiveData<DataWrapper<String>>
}