package edu.uoc.easyorderfront.ui.restaurant

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepository
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch
import java.lang.Exception

class RestaurantProfileViewModel (
    private val repository: RestaurantRepository
) : ViewModel() {
    val restaurantProfile = MutableLiveData<DataWrapper<Restaurant>>()
    private val TAG = "RestaurantProfileViewModel"

    fun getRestaurant(restaurantId: String) {
        viewModelScope.launch {
            try {
                restaurantProfile.postValue(DataWrapper.loading(null))

                repository.getRestaurant(restaurantId).let { restaurant ->
                    Log.d(TAG, "GetRestaurant: $restaurant")
                    restaurantProfile.postValue(DataWrapper.success(restaurant))
                }

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                restaurantProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                restaurantProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}