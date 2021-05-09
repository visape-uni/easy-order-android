package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.menu.CategoryDTO

data class Category(
    val uid: String?,
    val name: String? = null,
    val description: String? = null,
    val dishes: MutableList<Dish>? = ArrayList()
) {
    fun convertToDTO(): CategoryDTO {
        val dishesList = dishes?.map { dish -> dish.convertToDTO() }?.toMutableList()
        return CategoryDTO(uid, name, description, dishesList)
    }
}