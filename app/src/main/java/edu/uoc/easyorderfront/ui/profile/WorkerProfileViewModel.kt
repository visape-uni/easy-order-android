package edu.uoc.easyorderfront.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uoc.easyorderfront.data.authentication.AuthenticationRepository
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.utils.DataWrapper

class WorkerProfileViewModel(private val repository: AuthenticationRepository): ViewModel() {
    val workerProfile = MutableLiveData<DataWrapper<Worker>>()
    private val TAG = "WorkerProfileViewModel"

    fun getWorkerProfile(id: String) {

    }
}