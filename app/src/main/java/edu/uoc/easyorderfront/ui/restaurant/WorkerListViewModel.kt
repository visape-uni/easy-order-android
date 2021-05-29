package edu.uoc.easyorderfront.ui.restaurant

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepository
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class WorkerListViewModel(
        val repository: RestaurantRepository
): ViewModel() {
    val restaurantLiveData = MutableLiveData<DataWrapper<Restaurant>>()
    private val TAG = "WorkersListViewModel"

    fun removeWorkerRestaurant(restaurantId: String, workerId: String) {
        viewModelScope.launch {
            try {
                restaurantLiveData.postValue(DataWrapper.loading(restaurantLiveData.value?.data))

                repository.removeWorker(restaurantId, workerId).let { removeResponse ->
                    Log.d(TAG, "RemoveWorker: $removeResponse")
                    restaurantLiveData.postValue(DataWrapper.success(restaurantLiveData.value?.data))
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                when(easyOrderException.message) {
                    InternalErrorMessages.ERROR_WORKER_DOESNT_EXIST -> {
                        restaurantLiveData.postValue(DataWrapper.error(UIMessages.ERROR_TRABAJADOR_NO_EXISTE))
                    }
                    else -> {
                        restaurantLiveData.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                    }
                }
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                restaurantLiveData.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}