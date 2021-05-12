package edu.uoc.easyorderfront.ui.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.menu.MenuRepository
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepository
import edu.uoc.easyorderfront.data.table.TableRepository
import edu.uoc.easyorderfront.domain.model.Menu
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class MenuRestaurantViewModel(
        private val repository: MenuRepository,
        private val restaurantRepository: RestaurantRepository,
        private val tableRepository: TableRepository
) : ViewModel() {
    private val TAG = "MenuRestaurantViewModel"

    val order = MutableLiveData<Order>()
    val orderPrice = MutableLiveData<Double>()
    val menu = MutableLiveData<DataWrapper<Menu>>()
    val restaurantProfile = MutableLiveData<DataWrapper<Restaurant>>()
    val tableStateChanged = MutableLiveData<DataWrapper<Table>>()

    fun changeTableState(tableId: String, newState: String) {
        viewModelScope.launch {
            try {
                tableStateChanged.postValue(DataWrapper.loading(null))

                tableRepository.changeTableState(tableId, newState).let {tableResponse ->
                    Log.d(TAG, "ChangeTableState: $tableResponse")
                    tableStateChanged.postValue(DataWrapper.success(tableResponse))
                }

            }  catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                tableStateChanged.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTION CUANDO LA MESA YA ESTA OCUPADA
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                tableStateChanged.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }

    fun getMenu(restaurantId: String?) {
        viewModelScope.launch {
            menu.postValue(DataWrapper.loading(null))
            try {
                if (restaurantId != null) {
                    repository.getMenu(restaurantId).let { menuResponse ->
                        Log.d(TAG, "GetMenu: $menuResponse")
                        menu.postValue(DataWrapper.success(menuResponse))
                    }
                } else {
                    throw EasyOrderException(UIMessages.ERROR_ID_RESTAURANT_NUL)
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                menu.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                menu.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
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
                restaurantProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                restaurantProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}