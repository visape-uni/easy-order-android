package edu.uoc.easyorderfront.ui.order

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.order.OrderRepository
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class OrderWorkerViewModel(
        private val repository: OrderRepository
) : ViewModel() {
    val table = MutableLiveData<DataWrapper<Table>>()

    val lastOrder = MutableLiveData<DataWrapper<Order>>()

    private val TAG = "OrderWorkerViewModel"

    fun getLastOrderFromTable(tableId: String) {
        viewModelScope.launch {
            try {
                lastOrder.postValue(DataWrapper.loading(null))

                repository.getLastOrder(tableId).let { order->
                    Log.d(TAG, "GetLastOrderFromTable: $order")
                    lastOrder.postValue(DataWrapper.success(order))
                }

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                lastOrder.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTION CUANDO LA MESA YA ESTA OCUPADA
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                lastOrder.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }

}