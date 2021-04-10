package edu.uoc.easyorderfront.data

import android.content.Context
import edu.uoc.easyorderfront.domain.model.User

class SessionManager(context: Context) {
    private val sharedPreferencesName = "sessionPreferences"
    private val sharedPreferences =
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    private val accessTokenKey = "accessTokeKey"
    private val userIdKey = "userIdKey"

    fun getAccessToken(): String? {
        return sharedPreferences.getString(accessTokenKey, null)
    }

    fun saveAccessToken(accessToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString(accessTokenKey, accessToken)
        editor.apply()
    }

    fun clearAccessToken() {
        val editor = sharedPreferences.edit()
        editor.remove(accessTokenKey)
        editor.apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(userIdKey, null)
    }

    fun saveUserId(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(userIdKey, userId)
        editor.apply()
    }

    fun clearUserId() {
        val editor = sharedPreferences.edit()
        editor.remove(userIdKey)
        editor.apply()
    }
}