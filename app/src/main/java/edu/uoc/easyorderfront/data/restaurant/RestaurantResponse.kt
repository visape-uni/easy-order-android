package edu.uoc.easyorderfront.data.restaurant

import edu.uoc.easyorderfront.data.authentication.UserDTO
import edu.uoc.easyorderfront.data.table.TableDTO
import edu.uoc.easyorderfront.domain.model.Restaurant
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantDTO(
        val uid: String?,
        val name: String? = null,
        val street: String? = null,
        val city: String? = null,
        val zipCode: String? = null,
        val country: String? = null,
        val imageUrl: String? = null,
        val workers: MutableList<UserDTO>? = ArrayList(),
        val tables: MutableList<TableDTO>? = ArrayList(),
        val owner: UserDTO? = null
) {
    fun convertToModel(): Restaurant {
        val workersList = workers?.map { userDTO -> userDTO.convertToModel() }?.toMutableList()
        val tablesList = tables?.map { tableDTO -> tableDTO.convertToModel() }?.toMutableList()
        return Restaurant(uid, name, street, city, zipCode, country, imageUrl, workersList, tablesList, owner?.convertToModel())
    }
}