package edu.uoc.easyorderfront.ui.table

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.easyorderfront.data.error.EasyOrderException
import edu.uoc.easyorderfront.data.table.TableRepository
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.constants.UIMessages
import edu.uoc.easyorderfront.ui.utils.DataWrapper
import kotlinx.coroutines.launch
import java.lang.Exception

class CreateTableViewModel (
    private val repository: TableRepository
) : ViewModel() {
    val created = MutableLiveData<DataWrapper<Table>>()

    private val TAG = "CreateTableViewModel"

    fun createTable(table: Table) {
        viewModelScope.launch {
            try {
                created.postValue(DataWrapper.loading(table))

                repository.createTable(table).let { tableResponse ->
                    Log.d(TAG, "CreateTable: $tableResponse")
                    created.postValue(DataWrapper.success(tableResponse))
                }

            } catch (easyOrderException: EasyOrderException) {
                Log.e(TAG, easyOrderException.toString())
                created.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
                //TODO: TRATAR EXCEPTIONES ESPECIALES (SI HAY)
            } catch (e : Exception) {
                Log.e(TAG, e.toString())
                created.postValue(DataWrapper.error(UIMessages.ERROR_GENERICO))
            }
        }
    }
}