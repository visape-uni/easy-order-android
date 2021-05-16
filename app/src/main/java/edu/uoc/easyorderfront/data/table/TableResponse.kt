package edu.uoc.easyorderfront.data.table

import edu.uoc.easyorderfront.domain.model.Table
import kotlinx.serialization.Serializable

@Serializable
data class TableDTO(
    val uid: String?,
    val capacity: Int? = 0,
    val state: String? = null,
    val userId: String? = null
) {
    fun convertToModel(): Table {
        return Table(uid, capacity, state, userId)
    }
}