package edu.uoc.easyorderfront.ui.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.menu.MenuRepository
import edu.uoc.easyorderfront.domain.model.Dish
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class CreateDishViewModel(
        private val repository: MenuRepository
) : ViewModel() {
    val createdDish = MutableLiveData<DataWrapper<Dish>>()
    private val TAG = "CreateDishViewModel"

    fun createDish(dish: Dish, restaurantId: String, categoryId: String) {
        viewModelScope.launch {
            try {
                createdDish.postValue(DataWrapper.loading(null))

                repository.createDish(dish, restaurantId, categoryId)
                Log.d(TAG, "CreateCategory: $createdDish")
                createdDish.postValue(DataWrapper.success(dish))

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                createdDish.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                createdDish.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}