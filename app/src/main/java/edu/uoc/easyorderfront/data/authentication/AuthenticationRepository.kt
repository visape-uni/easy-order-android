package edu.uoc.easyorderfront.data.authentication

import com.google.firebase.auth.FirebaseUser
import edu.uoc.easyorderfront.domain.model.User

interface AuthenticationRepository {

    // Devuelve el usuario de firebase o una excepcion
    suspend fun login(email: String, password: String): FirebaseUser?

    // Devuelve el usuario registrado o una excepcion
    suspend fun register(user : User): User?

    // Devuelve el token del usuario actual
    suspend fun getIdToken(): String?

}