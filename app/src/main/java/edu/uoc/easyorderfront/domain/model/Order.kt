package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.order.OrderDTO
import java.io.Serializable


class Order(
        val uid: String?,
        var price: Float? = 0F,
        var state: String? = null,
        val startedTime: Long? = null,
        var orderedDishes: MutableList<OrderedDish>? = ArrayList(),
        val userId: String? = null
) : Serializable {
    fun converToDTO() : OrderDTO {
        val orderedDishesDTOList = orderedDishes?.map{orderedDish -> orderedDish.converToDTO() }?.toMutableList()

        return OrderDTO(uid, price, state, startedTime, orderedDishesDTOList)
    }
}