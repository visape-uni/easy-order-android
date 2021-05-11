package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.order.OrderDTO


class Order(
        val uid: String?,
        var price: Double? = 0.0,
        var state: String? = null,
        val startedTime: Long? = null,
        val orderedDishes: MutableList<OrderedDish>? = ArrayList()
) {
    fun converToDTO() : OrderDTO {
        val orderedDishesDTOList = orderedDishes?.map{orderedDish -> orderedDish.converToDTO() }?.toMutableList()

        return OrderDTO(uid, price, state, startedTime, orderedDishesDTOList)
    }
}