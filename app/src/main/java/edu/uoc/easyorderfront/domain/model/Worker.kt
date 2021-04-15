package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.authentication.UserDTO

data class Worker(
    @Transient override val uid:String?,
    @Transient  override val username:String?,
    @Transient  override val email:String?,
    @Transient  override val password:String? = null,
        val restaurant: Restaurant?): User(uid, username, email, password, false) {
    override fun convertToDTO(): UserDTO {
        return UserDTO(uid, username, email, password, isClient, restaurant?.convertToDTO())
    }
}