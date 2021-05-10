package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.order.OrderedDishDTO

class OrderedDish(
        val uid: String?,
        val quantity: Int? = 0,
        val totalPrice: Double? = 0.0
        //val dish: Dish?
) {
    fun converToDTO() : OrderedDishDTO {
        return OrderedDishDTO(uid, quantity, totalPrice)
    }
}
