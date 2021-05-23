package edu.uoc.easyorderfront.ui.restaurant

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepository
import edu.uoc.easyorderfront.domain.model.Worker
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class AddWorkerDialogViewModel(
        private val repository: RestaurantRepository
) : ViewModel() {
    val created = MutableLiveData<DataWrapper<Worker>>()
    private val TAG = "AddWorkerViewModel"

    fun addWorkerRestaurant(restaurantId: String, workerId: String) {
        viewModelScope.launch {
            try {
                created.postValue(DataWrapper.loading(null))

                repository.addWorker(restaurantId, workerId).let { workerResponse ->
                    Log.d(TAG, "AddWorker: $workerResponse")
                    created.postValue(DataWrapper.success(workerResponse))
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                when(easyOrderException.message) {
                    InternalErrorMessages.ERROR_WORKER_DOESNT_EXIST -> {
                        created.postValue(DataWrapper.error(UIMessages.ERROR_TRABAJADOR_NO_EXISTE))
                    }
                    else -> {
                        created.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                    }
                }
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                created.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }

}