package edu.uoc.easyorderfront.domain.model

data class Worker(
     override val uid:String?,
     override val username:String?,
     override val email:String?,
     override val password:String? = null): User(uid, username, email, password, false) {
}