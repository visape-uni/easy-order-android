package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.authentication.UserDTO

data class Worker(
     override val uid:String?,
     override val username:String?,
     override val email:String?,
     override val password:String? = null,
        val restaurant: Restaurant?): User(uid, username, email, password, false) {
    override fun convertToDTO(): UserDTO {
        return UserDTO(uid, username, email, password, isClient, restaurant?.convertToDTO())
    }
}