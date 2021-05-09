package edu.uoc.easyorderfront.domain.model

import edu.uoc.easyorderfront.data.menu.MenuDTO

data class Menu (
    val uid: String?,
    val categories: MutableList<Category>? = ArrayList()
) {
    fun convertToDTO(): MenuDTO {
        val categoriesList = categories?.map{ category -> category.convertToDTO() }?.toMutableList()
        return MenuDTO(uid, categoriesList)
    }
}