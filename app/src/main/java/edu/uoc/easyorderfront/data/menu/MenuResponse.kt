package edu.uoc.easyorderfront.data.menu

import edu.uoc.easyorderfront.domain.model.Category
import edu.uoc.easyorderfront.domain.model.Dish
import edu.uoc.easyorderfront.domain.model.Menu
import kotlinx.serialization.Serializable

@Serializable
data class MenuDTO(
    val uid: String?,
    val categories: MutableList<CategoryDTO>? = ArrayList()
) {
    fun convertToModel(): Menu {
        val categoriesList = categories?.map{ categoryDTO -> categoryDTO.convertToModel() }?.toMutableList()
        return Menu(uid, categoriesList)
    }
}


@Serializable
data class CategoryDTO(
    val uid: String?,
    val name: String? = null,
    val description: String? = null,
    val dishes: MutableList<DishDTO>? = ArrayList()
) {
    fun convertToModel(): Category {
        val dishesList = dishes?.map{ dishDTO -> dishDTO.convertToModel() }?.toMutableList()
        return Category(uid, name, description, dishesList)
    }
}


@Serializable
data class DishDTO(
    val uid: String?,
    val name: String? = null,
    val description: String? = null,
    val price: Float = 0F,
    val aliments: MutableList<String>? = ArrayList(),
    val calories: Int? = 0
) {
    fun convertToModel(): Dish {
        return Dish(uid, name, description, price, aliments, calories)
    }
}