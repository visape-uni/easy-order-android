package edu.uoc.easyorderfront.data.order

import edu.uoc.easyorderfront.data.menu.DishDTO
import edu.uoc.easyorderfront.domain.model.Order
import edu.uoc.easyorderfront.domain.model.OrderedDish
import kotlinx.serialization.Serializable

@Serializable
data class OrderDTO(
        val uid: String? = null,
        val price: Double? = 0.0,
        val state: String? = null,
        val startedTime: Long? = null,
        val orderedDishes: MutableList<OrderedDishDTO>? = ArrayList()
) {
    fun convertToModel(): Order {
        val orderedDishesList = orderedDishes?.map { orderedDishDTO -> orderedDishDTO.convertToModel() }?.toMutableList()

        return Order(uid, price, state, startedTime, orderedDishesList)
    }
}

@Serializable
data class OrderedDishDTO(
        val uid: String? = null,
        val quantity: Int? = 0,
        val totalPrice: Double? = 0.0,
        val categoryId: String? = null,
        val dish: DishDTO? = null
) {
    fun convertToModel(): OrderedDish {
        return OrderedDish(uid, quantity, totalPrice, categoryId, dish?.convertToModel())
    }
}

