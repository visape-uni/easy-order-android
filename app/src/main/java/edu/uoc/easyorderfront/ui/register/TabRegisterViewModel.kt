package edu.uoc.easyorderfront.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.data.authentication.User
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.MyException
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch
import java.lang.Exception

class TabRegisterViewModel(
        private val repository: AuthenticationRepository
) : ViewModel() {
    val registered = MutableLiveData<DataWrapper<User>>()
    lateinit var login : MutableLiveData<DataWrapper<FirebaseUser?>>
    lateinit var token : MutableLiveData<DataWrapper<String>>

    private val TAG = "TabRegisterViewModel"

    fun register(username: String, email:String, password: String, isClient : Boolean) {
        viewModelScope.launch {
            try {
                // Loding
                registered.postValue(DataWrapper.loading(User(null, null, null)))

                repository.register(username, email, password, isClient).let { response ->
                    Log.d(TAG, "Register: $response")
                    registered.postValue(DataWrapper.success(response))
                }
            } catch (myException: MyException) {
                Log.e(TAG, myException.toString())
                when(myException.message) {
                    InternalErrorMessages.ERROR_USER_ALREADY_EXISTS -> {
                        registered.postValue(DataWrapper.error(UIMessages.ERROR_USUARIO_EXISTENTE))
                    }
                    else -> {
                        if (myException.message?.startsWith(InternalErrorMessages.ERROR_INTERNAL_GENERIC)!!) {
                            registered.postValue(DataWrapper.error(UIMessages.ERROR_BACKEND_ERROR))
                        } else {
                            registered.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                        }
                    }
                }
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                registered.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                login = repository.login(email, password)

            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                login = repository.login(email, password)
            }
        }
    }

    fun getTokenId() {
        viewModelScope.launch {
            try {
                token = repository.getIdToken()
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                token = repository.getIdToken()
            }
        }
    }
}