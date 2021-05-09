package edu.uoc.easyorderfront.data.menu

import edu.uoc.easyorderfront.domain.model.Menu

interface MenuRepository {
    suspend fun getMenu(restaurantId: String): Menu?
}