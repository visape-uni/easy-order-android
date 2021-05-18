package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.menu.DishDTO
import java.io.Serializable

data class Dish(
    val uid: String?,
    val name: String? = null,
    val description: String? = null,
    val price: Float = 0F,
    val aliments: MutableList<String>? = ArrayList(),
    val calories: Int? = 0
) : Serializable {
    fun convertToDTO(): DishDTO {
        return DishDTO(uid, name, description, price, aliments, calories)
    }
}