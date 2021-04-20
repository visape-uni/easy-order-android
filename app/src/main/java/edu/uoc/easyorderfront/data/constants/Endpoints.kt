package edu.uoc.easyorderfront.data.constants

object Endpoints {
    private const val easyOrderApiUrl = "https://easy-order-backend.herokuapp.com"

    private const val authenticationApi = "$easyOrderApiUrl/api"

    private const val userApi = "$easyOrderApiUrl/user"
    const val registerUrl = "$userApi/create"
    const val getUserUrl = "$userApi/get/"

    private const val restaurantApi = "$authenticationApi/restaurant"
    const val createRestaurantUrl = "$restaurantApi/create"
    const val getRestaurantUrl = "$restaurantApi/get/"


}