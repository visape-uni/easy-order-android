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

class CreateRestaurantViewModel(
        private val repository: RestaurantRepository
) : ViewModel() {
    val created = MutableLiveData<DataWrapper<Restaurant>>()

    private val TAG = "CreateRestaurantViewModel"

    fun createRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            try {
                created.postValue(DataWrapper.loading(restaurant))

                repository.createRestaurant(restaurant).let { restaurantResponse ->
                    Log.d(TAG, "CreateRestaurant: $restaurantResponse")
                    created.postValue(DataWrapper.success(restaurantResponse))
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                created.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                created.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}