package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.table.TableDTO
import java.io.Serializable

class Table(
    val uid: String?,
    val capacity: Int? = 0,
    val state: String? = null,
    var tableRef: String? = null
) : Serializable {
    fun convertToDTO(): TableDTO {
        return TableDTO(uid, capacity, state)
    }
}
