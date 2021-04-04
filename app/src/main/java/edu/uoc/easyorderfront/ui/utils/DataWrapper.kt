package edu.uoc.easyorderfront.ui.utils

data class DataWrapper<T> (val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): DataWrapper<T> {
            return DataWrapper(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): DataWrapper<T> {
            return DataWrapper(Status.ERROR, null, msg)
        }

        fun <T> loading(data: T?): DataWrapper<T> {
            return DataWrapper(Status.LOADING, data, null)
        }
    }
}