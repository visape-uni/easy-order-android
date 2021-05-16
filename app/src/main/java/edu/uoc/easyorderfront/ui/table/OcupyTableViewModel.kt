package edu.uoc.easyorderfront.ui.table

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.table.TableRepository
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch

class OcupyTableViewModel(
        private val repository: TableRepository
) : ViewModel() {
    val tableStateChanged = MutableLiveData<DataWrapper<Table>>()

    val table = MutableLiveData<DataWrapper<Table>>()

    private val TAG = "OcupyTableViewModel"

    fun changeTableState(tableId: String, newUserId:String, newState: String) {
        viewModelScope.launch {
            try {
                tableStateChanged.postValue(DataWrapper.loading(null))

                repository.changeTableState(tableId, newUserId, newState).let {tableResponse ->
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

    fun getTable(restaurantId: String, tableId: String) {
        viewModelScope.launch {
            try {
                table.postValue(DataWrapper.loading(null))
                repository.getTable(restaurantId, tableId).let { tableResponse ->
                    Log.d(TAG, "GetTable: $tableResponse")
                    table.postValue(DataWrapper.success(tableResponse))
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
}