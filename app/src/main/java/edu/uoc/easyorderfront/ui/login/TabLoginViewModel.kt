package edu.uoc.easyorderfront.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.profile.ProfileRepository
import edu.uoc.easyorderfront.domain.model.User
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch
import java.lang.Exception

class TabLoginViewModel (private val repository: AuthenticationRepository,
                         private val profileRepository: ProfileRepository) : ViewModel() {
    lateinit var isLogged  : MutableLiveData<DataWrapper<FirebaseUser?>>
    lateinit var getTokenResult: MutableLiveData<DataWrapper<GetTokenResult>>

    var userProfile = MutableLiveData<DataWrapper<User?>>()

    private val TAG = "TabLoginViewModel"

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                isLogged = repository.login(email, password)

            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                isLogged = repository.login(email, password)
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
                userProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                userProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}