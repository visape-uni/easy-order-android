package edu.uoc.easyorderfront.ui.payment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stripe.android.paymentsheet.PaymentSheet
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.payment.PaymentRepository
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.domain.model.PaymentResponse
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class PaymentMethodsViewModel(
    private val paymentRepository: PaymentRepository
): ViewModel() {
    val order = MutableLiveData<DataWrapper<Order>>()
    val paymentResponse = MutableLiveData<DataWrapper<PaymentResponse>>()
    lateinit var paymentSheet: PaymentSheet

    private val TAG = "PaymentMethodsViewModel"

    fun fetchInitData(order: Order) {
        viewModelScope.launch {
            try {
                paymentResponse.postValue(DataWrapper.loading(null))

                paymentRepository.fetchInitData(order).let { response ->
                    Log.d(TAG, "FetchInitData: $response")
                    paymentResponse.postValue(DataWrapper.success(response))
                }
            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                paymentResponse.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                paymentResponse.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}