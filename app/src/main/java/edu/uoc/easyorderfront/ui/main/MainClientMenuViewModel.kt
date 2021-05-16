package edu.uoc.easyorderfront.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.profile.ProfileRepository
import edu.uoc.easyorderfront.domain.model.User
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class MainClientMenuViewModel(
        private val profileRepository: ProfileRepository
): ViewModel() {

    val clientProfile = MutableLiveData<DataWrapper<User?>>()
    private val TAG = "MainClientMenuViewModel"

    fun getClientProfile(id: String) {
        viewModelScope.launch {
            try {
                clientProfile.postValue(DataWrapper.loading(null))
                profileRepository.getProfile(id).let { userResponse ->
                    Log.d(TAG, "GetWorkerProfile: $userResponse")

                    clientProfile.postValue(DataWrapper.success(userResponse))
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                clientProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                clientProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}