package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.order.OrderedDishDTO
import java.io.Serializable

class OrderedDish(
        val uid: String?,
        val quantity: Int? = 0,
        val totalPrice: Double? = 0.0,
        val dish: Dish?
) : Serializable {
    fun converToDTO() : OrderedDishDTO {
        return OrderedDishDTO(uid, quantity, totalPrice, dish?.convertToDTO())
    }
}
