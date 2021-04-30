package edu.uoc.easyorderfront.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.ui.utils.DataWrapper

class OrderWorkerViewModel() : ViewModel() {
    val table = MutableLiveData<DataWrapper<Table>>()
}