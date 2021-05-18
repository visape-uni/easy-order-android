package edu.uoc.easyorderfront.ui.order

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.order.OrderRepository
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class OrderClientViewModel(
        private val repository: OrderRepository
) : ViewModel() {
    val orderSaved = MutableLiveData<DataWrapper<Order>>()
    val order = MutableLiveData<Order>()

    private val TAG = "OrderClientViewModel"

    fun saveOrder(restaurantId: String, order: Order) {
        viewModelScope.launch {
            try {
                orderSaved.postValue(DataWrapper.loading(null))

                repository.saveOrder(restaurantId, order).let {orderResponse ->
                    Log.d(TAG, "SaveOrder: $orderResponse")
                    orderSaved.postValue(DataWrapper.success(orderResponse))
                }

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                orderSaved.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTION CUANDO LA MESA YA ESTA OCUPADA
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                orderSaved.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}