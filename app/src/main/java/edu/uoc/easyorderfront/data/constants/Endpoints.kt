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
    const val addWorkerUrl = "$restaurantApi/addWorker/"
    const val removeWorkerUrl = "$restaurantApi/removeWorker/"

    private const val tableApi = "$authenticationApi/table"
    const val createTableUrl = "$tableApi/create"
    const val getAllTablesUrl = "$tableApi/getAll/"
    const val changeTableStateUrl = "$tableApi/changeState/"
    const val getTableUrl = "$tableApi/get/"
    const val askForBillUrl = "$tableApi/askForTheBill/"

    private const val orderApi = "$authenticationApi/order"
    const val getLastOrderUrl = "$orderApi/getLast/"
    const val saveOrderUrl = "$orderApi/saveOrder/"

    private const val menuApi = "$authenticationApi/menu"
    const val getMenuUrl = "$menuApi/getFromRestaurant/"
    const val createCategoryUrl = "$menuApi/createCategory/"
    const val createDishUrl = "$menuApi/createDish/"
    const val deleteDishUrl = "$menuApi/deleteDish/"

}