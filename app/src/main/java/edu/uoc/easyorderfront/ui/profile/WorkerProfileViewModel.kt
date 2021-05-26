package edu.uoc.easyorderfront.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.profile.ProfileRepository
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class WorkerProfileViewModel(private val profileRepository: ProfileRepository): ViewModel() {
    val workerProfile = MutableLiveData<DataWrapper<Worker?>>()
    val restaurantMenu = MutableLiveData<Boolean?>()
    private val TAG = "WorkerProfileViewModel"

    fun getWorkerProfile(id: String) {
        viewModelScope.launch {
            try {
                workerProfile.postValue(DataWrapper.loading(null))
                profileRepository.getProfile(id).let { userResponse ->
                    Log.d(TAG, "GetWorkerProfile: $userResponse")
                    val worker = userResponse as Worker

                    workerProfile.postValue(DataWrapper.success(worker))
                    Log.d(TAG, "ownerResponse ${userResponse.isOwner}")
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                workerProfile.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                workerProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}