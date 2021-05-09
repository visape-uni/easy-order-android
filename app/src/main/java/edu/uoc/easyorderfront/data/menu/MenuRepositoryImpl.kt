package edu.uoc.easyorderfront.data.menu

import android.util.Log
import edu.uoc.easyorderfront.domain.model.Category
import edu.uoc.easyorderfront.domain.model.Dish
import edu.uoc.easyorderfront.domain.model.Menu

class MenuRepositoryImpl(private val menuBackendDataSource: MenuBackendDataSource): MenuRepository {
    private val TAG = "MenuRepositoryImpl"

    override suspend fun getMenu(restaurantId: String): Menu? {
        val menuDTO = menuBackendDataSource.getMenu(restaurantId)
        Log.d(TAG, "Get Menu response $menuDTO")

        return menuDTO?.convertToModel()
    }

    override suspend fun createCategory(category: Category, restaurantId: String) {
        menuBackendDataSource.createCategory(category.convertToDTO(), restaurantId)
    }

    override suspend fun createDish(dish: Dish, restaurantId: String, categoryId: String) {
        menuBackendDataSource.createDish(dish.convertToDTO(), restaurantId, categoryId)
    }

    override suspend fun deleteDish(restaurantId: String?, categoryId: String?, dishId: String?) {
        menuBackendDataSource.deleteDish(restaurantId, categoryId, dishId)
    }

}