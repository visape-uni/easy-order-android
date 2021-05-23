package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.authentication.UserDTO
import java.io.Serializable


open class  User (
        open val uid:String?,
        open val username:String? = null,
        open val email:String? = null,
        open val password:String? = null,
        open val isClient:Boolean? = null,
        val tableId:String? = null
): Serializable {
    open fun convertToDTO(): UserDTO {
        return UserDTO(uid, username, email, password, isClient, tableId = tableId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (uid != other.uid) return false

        return true
    }

    override fun hashCode(): Int {
        return uid?.hashCode() ?: 0
    }


}