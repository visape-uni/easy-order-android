package edu.uoc.easyorderfront.ui.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.menu.MenuRepository
import edu.uoc.easyorderfront.data.restaurant.RestaurantRepository
import edu.uoc.easyorderfront.domain.model.Menu
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class EditarMenuViewModel(
    private val repository: MenuRepository,
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val TAG = "EditarMenuViewModel"

    val menu = MutableLiveData<DataWrapper<Menu>>()
    val menuDeleted = MutableLiveData<DataWrapper<Menu>>()
    val restaurantProfile = MutableLiveData<DataWrapper<Restaurant>>()

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
                menu.postValue(DataWrapper.error(easyOrderException.message.toString()))
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
                restaurantProfile.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                restaurantProfile.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }

    fun deleteDish(restaurantId: String?, categoryId: String?, dishId: String?) {
        viewModelScope.launch {
            try {
                repository.deleteDish(restaurantId, categoryId, dishId)
                val dishes = menu.value?.data?.categories?.get(categoryId?.toInt()!! - 1)?.dishes!!
                for (i in dishes.indices) {
                    if (dishes.get(i).uid.equals(dishId)) {
                        dishes.removeAt(i)
                        break
                    }
                }

                menuDeleted.postValue(DataWrapper.success(menu.value!!.data))
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