package edu.uoc.easyorderfront.data.menu

import android.util.Log
import edu.uoc.easyorderfront.domain.model.Menu

class MenuRepositoryImpl(private val menuBackendDataSource: MenuBackendDataSource): MenuRepository {
    private val TAG = "MenuRepositoryImpl"

    override suspend fun getMenu(restaurantId: String): Menu? {
        val menuDTO = menuBackendDataSource.getMenu(restaurantId)
        Log.d(TAG, "Get Menu response $menuDTO")

        return menuDTO?.convertToModel
    }
}