package edu.uoc.easyorderfront.data.authentication

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import edu.uoc.easyorderfront.domain.model.User
import edu.uoc.easyorderfront.ui.utils.DataWrapper

interface AuthenticationRepository {

    // Devuelve el usuario de firebase o una excepcion
    suspend fun login(email: String, password: String): MutableLiveData<DataWrapper<FirebaseUser?>>

    // Devuelve el usuario registrado o una excepcion
    suspend fun register(user : User): User?

    // Devuelve el token del usuario actual
    suspend fun getIdToken(): MutableLiveData<DataWrapper<GetTokenResult>>

}