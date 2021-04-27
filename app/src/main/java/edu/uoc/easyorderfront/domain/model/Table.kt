package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.table.TableDTO
import kotlinx.serialization.Serializable

@Serializable
class Table(
    val uid: String?,
    val capacity: Int? = 0,
    val state: String? = null
) {
    fun convertToDTO(): TableDTO {
        return TableDTO(uid, capacity, state)
    }
}