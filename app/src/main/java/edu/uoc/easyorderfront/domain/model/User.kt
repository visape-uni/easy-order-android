package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.authentication.UserDTO


open class  User (
        open val uid:String?,
        open val username:String? = null,
        open val email:String? = null,
        open val password:String? = null,
        open val isClient:Boolean? = null,
        val tableId:String? = null
) {
    open fun convertToDTO(): UserDTO {
        return UserDTO(uid, username, email, password, isClient, tableId = tableId)
    }
}