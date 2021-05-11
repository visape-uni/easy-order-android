package edu.uoc.easyorderfront.ui.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uoc.easyorderfront.data.menu.MenuRepository

class AddDishToOrderViewModel(
        private val repository: MenuRepository
) : ViewModel() {
    private val TAG = "AddDishToOrderViewModel"
    val quantity = MutableLiveData<Int>(1)

    fun setQuantity(value: Int) {
        if (value > 0) {
            quantity.postValue(value)
        }
    }
}