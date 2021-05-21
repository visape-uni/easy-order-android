package edu.uoc.easyorderfront.domain.model


import edu.uoc.easyorderfront.data.authentication.UserDTO

class Worker(
        uid:String?,
        username:String?,
        email:String?,
        password:String? = null,
        var isOwner:Boolean?,
        var restaurant: Restaurant?): User(uid, username, email, password, false) {
    override fun convertToDTO(): UserDTO {
        return UserDTO(uid, username, email, password, isClient, isOwner,null, restaurant?.convertToDTO())
    }
}