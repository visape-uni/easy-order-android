package edu.uoc.easyorderfront.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.domain.model.User
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class TabRegisterViewModel(
        private val repository: AuthenticationRepository
) : ViewModel() {
    val registered = MutableLiveData<DataWrapper<User>>()
    lateinit var login : MutableLiveData<DataWrapper<FirebaseUser?>>
    lateinit var getTokenResult : MutableLiveData<DataWrapper<GetTokenResult>>

    private val TAG = "TabRegisterViewModel"

    fun register(user: User) {
        viewModelScope.launch {
            try {
                // Loding
                registered.postValue(DataWrapper.loading(user))

                repository.register(user).let { userResponse ->
                    Log.d(TAG, "Register: $userResponse")
                    registered.postValue(DataWrapper.success(userResponse))
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                when(easyOrderException.message) {
                    InternalErrorMessages.ERROR_USER_ALREADY_EXISTS -> {
                        registered.postValue(DataWrapper.error(UIMessages.ERROR_USUARIO_EXISTENTE))
                    }
                    InternalErrorMessages.ERROR_BACKEND_TIMEOUT -> {
                        registered.postValue(DataWrapper.error(UIMessages.ERROR_BACKEND_TIMEOUT))
                    }
                    else -> {
                        if (easyOrderException.message?.startsWith(InternalErrorMessages.ERROR_INTERNAL_GENERIC)!!) {
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
                getTokenResult = repository.getIdToken()
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                getTokenResult = repository.getIdToken()
            }
        }
    }
}