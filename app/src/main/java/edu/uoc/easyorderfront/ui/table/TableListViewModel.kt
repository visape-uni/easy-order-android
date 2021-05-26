package edu.uoc.easyorderfront.ui.table

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepository
import edu.uoc.easyorderfront.data.table.TableRepository
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class TableListViewModel (
    private val repository: TableRepository,
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    val restaurantProfile = MutableLiveData<DataWrapper<Restaurant>>()
    val tables = MutableLiveData<DataWrapper<List<Table>>>()

    private val TAG = "TableViewModel"

    fun getTables(restaurantId: String?) {
        viewModelScope.launch {
            tables.postValue(DataWrapper.loading(null))
            try {
                if (restaurantId != null) {
                    repository.getTables(restaurantId).let { tablesList ->
                        Log.d(TAG, "GetTables: $tablesList")
                        tables.postValue(DataWrapper.success(tablesList))
                    }
                } else {
                    throw EasyOrderException(UIMessages.ERROR_ID_RESTAURANT_NUL)
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                tables.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                tables.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }

    fun getRestaurant(restaurantId: String?) {
        viewModelScope.launch {
            try {
                restaurantProfile.postValue(DataWrapper.loading(null))

                if (restaurantId != null) {
                    restaurantRepository.getRestaurant(restaurantId).let { restaurant ->
                        Log.d(TAG, "GetRestaurant: $restaurant")
                        restaurantProfile.postValue(DataWrapper.success(restaurant))
                    }
                } else {
                    throw EasyOrderException(UIMessages.ERROR_ID_RESTAURANT_NUL)
                }

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                restaurantProfile.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                restaurantProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}