package edu.uoc.easyorderfront.data.constants

object Endpoints {
    private const val easyOrderApiUrl = "https://easy-order-backend.herokuapp.com"

    private const val userApi = "$easyOrderApiUrl/user"
    const val registerUrl = "$userApi/create"


}