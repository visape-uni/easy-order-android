package edu.uoc.easyorderfront.data.menu

import edu.uoc.easyorderfront.domain.model.Category
import edu.uoc.easyorderfront.domain.model.Dish
import edu.uoc.easyorderfront.domain.model.Menu

interface MenuRepository {
    suspend fun getMenu(restaurantId: String): Menu?
    suspend fun createCategory(category: Category, restaurantId: String)
    suspend fun createDish(dish: Dish, restaurantId: String, categoryId: String)
    suspend fun deleteDish(restaurantId: String?, categoryId: String?, dishId: String?)
}