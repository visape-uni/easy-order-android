package edu.uoc.easyorderfront.domain.model


import edu.uoc.easyorderfront.data.authentication.UserDTO
import java.io.Serializable

class Worker(
        uid:String?,
        username:String? = null,
        email:String? = null,
        password:String? = null,
        var isOwner:Boolean? = false,
        var restaurant: Restaurant? = null) : Serializable, User(uid, username, email, password, false) {
    override fun convertToDTO(): UserDTO {
        return UserDTO(uid, username, email, password, isClient, isOwner,null, restaurant?.convertToDTO())
    }

}