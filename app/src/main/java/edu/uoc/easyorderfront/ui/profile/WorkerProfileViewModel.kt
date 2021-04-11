package edu.uoc.easyorderfront.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.profile.ProfileRepository
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch
import java.lang.Exception

class WorkerProfileViewModel(private val repository: ProfileRepository): ViewModel() {
    val workerProfile = MutableLiveData<DataWrapper<Worker?>>()
    private val TAG = "WorkerProfileViewModel"

    fun getWorkerProfile(id: String) {
        viewModelScope.launch {
            try {
                workerProfile.postValue(DataWrapper.loading(null))

                repository.getProfile(id).let { userResponse ->
                    Log.d(TAG, "GetWorkerProfile: $userResponse")
                    workerProfile.postValue(DataWrapper.success(userResponse as Worker))
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                workerProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                workerProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}