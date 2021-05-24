package edu.uoc.easyorderfront.ui.payment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.table.TableRepository
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.EasyOrderConstants
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class WaitingBillViewModel (
    private val tableRepository: TableRepository
) : ViewModel() {
    private val TAG = "WaitingBillViewModel"
    val paid = MutableLiveData<DataWrapper<Table>>()
    val tableClosed = MutableLiveData<DataWrapper<Table>>()

    fun getOrder(tableId: String) {
        viewModelScope.launch {
            try {
                paid.postValue(DataWrapper.loading(null))

                val tableSplit = tableId.split("/")

                tableRepository.getTable(tableSplit.get(0), tableSplit.get(1)).let { table ->
                    Log.d(TAG, "getTable: $table")
                    paid.postValue(DataWrapper.success(table))
                }

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                paid.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTION CUANDO LA MESA YA ESTA OCUPADA
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                paid.postValue(DataWrapper.error(UIMessages.ERROR_OBTENIENDO_MESA))
            }
        }
    }

    fun endOrder(tableId: String) {
        viewModelScope.launch {
            try {
                paid.postValue(DataWrapper.loading(null))

                tableRepository.changeTableState(tableId, "", EasyOrderConstants.EMPTY_TABLE_STATE).let {table ->
                    Log.d(TAG, "changingTableState: $table")
                    tableClosed.postValue(DataWrapper.success(table))
                }

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                paid.postValue(DataWrapper.error(easyOrderException.message.toString()))
                //TODO: TRATAR EXCEPTION CUANDO LA MESA YA ESTA OCUPADA
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                paid.postValue(DataWrapper.error(UIMessages.ERROR_OBTENIENDO_MESA))
            }
        }
    }

}