package edu.uoc.easyorderfront.ui.payment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.table.TableRepository
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class PaymentMethodViewModel(
    private val tableRepository: TableRepository
) : ViewModel() {
    private val TAG = "PaymentMethodViewModel"
    val pay = MutableLiveData<DataWrapper<Boolean>>()

    fun askForTheBill(tableId: String) {
        viewModelScope.launch {
            try {
                pay.postValue(DataWrapper.loading(null))

                tableRepository.askForTheBill(tableId).let { billResponse ->
                    Log.d(TAG, "AskForTheBill: $billResponse")
                    pay.postValue(DataWrapper.success(billResponse))
                }

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                pay.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTION CUANDO LA MESA YA ESTA OCUPADA
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                pay.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}