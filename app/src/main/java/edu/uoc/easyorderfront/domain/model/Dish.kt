package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.menu.DishDTO

data class Dish(
    val uid: String?,
    val name: String? = null,
    val description: String? = null,
    val price: Double = 0.0,
    val aliments: MutableList<String>? = ArrayList(),
    val calories: Int? = 0
) {
    fun convertToDTO(): DishDTO {
        return DishDTO(uid, name, description, price, aliments, calories)
    }
}