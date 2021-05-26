package edu.uoc.easyorderfront.ui.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.menu.MenuRepository
import edu.uoc.easyorderfront.domain.model.Category
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class CreateCategoryViewModel(
        private val repository: MenuRepository
) : ViewModel() {
    val createdCategory = MutableLiveData<DataWrapper<Category>>()
    private val TAG = "CreateCategoryViewModel"

    fun createCategory(category: Category, restaurantId: String) {
        viewModelScope.launch {
            try {
                createdCategory.postValue(DataWrapper.loading(null))

                repository.createCategory(category, restaurantId)
                Log.d(TAG, "CreateCategory: $category")
                createdCategory.postValue(DataWrapper.success(category))

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                createdCategory.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                createdCategory.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}