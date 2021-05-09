package edu.uoc.easyorderfront.data.menu

import android.util.Log
import edu.uoc.easyorderfront.data.constants.Endpoints
import edu.uoc.easyorderfront.data.constants.InternalErrorMessages
import edu.uoc.easyorderfront.data.error.EasyOrderException
import io.ktor.client.*
import io.ktor.client.request.*

class MenuBackendDataSource(private val httpClient: HttpClient) {
    private val TAG = "MenuBackendDataSource"

    suspend fun getMenu(restaurantId: String): MenuDTO? {
        try {
            return httpClient
                .get<MenuDTO>(Endpoints.getMenuUrl + restaurantId)
        } catch (t: Throwable) {
            Log.w(TAG, "Error obteniendo menu", t)
            // TODO: TRATAR EXCEPCIONES
            throw EasyOrderException(InternalErrorMessages.ERROR_UNKNOWN_EXCEPTION)
        }
    }

    suspend fun createCategory(category: CategoryDTO, restaurantId: String) {
        httpClient.post<CategoryDTO>(Endpoints.createCategoryUrl + restaurantId) {
            body = category
        }
    }

    suspend fun createDish(dish: DishDTO, restaurantId: String, categoryId: String) {
        httpClient.post<DishDTO>(Endpoints.createDishUrl + restaurantId + "/" + categoryId) {
            body = dish
        }
    }

    suspend fun deleteDish(restaurantId: String?, categoryId: String?, dishId: String?) {
        httpClient.delete<Boolean>(Endpoints.deleteDishUrl + restaurantId + "/" + categoryId + "/" + dishId)
    }
}