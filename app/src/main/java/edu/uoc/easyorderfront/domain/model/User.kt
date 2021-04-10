package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.authentication.UserDTO

open class User (
        open val uid:String?,
        open val username:String?,
        open val email:String?,
        open val password:String? = null,
        open val isClient:Boolean? = null
) {
    open fun convertToDTO(): UserDTO {
        return UserDTO(uid, username, email, password, isClient)
    }
}