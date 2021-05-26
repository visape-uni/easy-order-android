package edu.uoc.easyorderfront.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.profile.ProfileRepository
import edu.uoc.easyorderfront.domain.model.User
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class TabLoginViewModel (private val repository: AuthenticationRepository,
                         private val profileRepository: ProfileRepository) : ViewModel() {
    val isLogged = MutableLiveData<DataWrapper<FirebaseUser?>>()
    val getTokenResult = MutableLiveData<DataWrapper<String?>>()

    var userProfile = MutableLiveData<DataWrapper<User?>>()

    private val TAG = "TabLoginViewModel"

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                isLogged.postValue(DataWrapper.loading(null))
                repository.login(email, password).let { loginResponse ->
                    Log.d(TAG, "Login: $loginResponse")
                    isLogged.postValue(DataWrapper.success(loginResponse))
                }
            } catch (e: EasyOrderException) {
                Log.e(TAG, e.toString())
                isLogged.postValue(DataWrapper.error(e.message.toString()))
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                isLogged.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }

    fun getTokenId() {
        viewModelScope.launch {
            try {
                getTokenResult.postValue(DataWrapper.loading(null))

                repository.getIdToken().let { tokenResponse ->
                    Log.d(TAG, "Register: $tokenResponse")
                    getTokenResult.postValue(DataWrapper.success(tokenResponse))
                }
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                getTokenResult.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }

    fun getProfile(id: String) {
        viewModelScope.launch {
            try {
                userProfile.postValue(DataWrapper.loading(null))

                profileRepository.getProfile(id).let { userResponse ->
                    Log.d(TAG, "GetUserProfile: $userResponse")

                    userProfile.postValue(DataWrapper.success(userResponse))
                }

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                userProfile.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                userProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}