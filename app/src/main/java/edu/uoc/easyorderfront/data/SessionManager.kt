package edu.uoc.easyorderfront.data

import android.content.Context
import com.google.gson.Gson
import edu.uoc.easyorderfront.domain.model.Restaurant
import edu.uoc.easyorderfront.domain.model.Table
import edu.uoc.easyorderfront.domain.model.User
import edu.uoc.easyorderfront.domain.model.Worker

class SessionManager(context: Context) {
    private val sharedPreferencesName = "sessionPreferences"
    private val sharedPreferences =
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    private val accessTokenKey = "accessTokeKey"
    private val userIdKey = "userIdKey"
    private val userKey = "userKey"
    private val userType = "userType"

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

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(userKey, null)
        val userType = sharedPreferences.getBoolean(userType, true)
        if (userType) {
            return Gson().fromJson(userJson, User::class.java)
        } else {
            val worker = Gson().fromJson(userJson, Worker::class.java)
            return worker;
        }

    }

    fun saveUser(user: User) {
        val editor = sharedPreferences.edit()
        val userJson : String?
        if (user.isClient != null && user.isClient!!) {
            userJson = Gson().toJson(user)
        } else {
            userJson = Gson().toJson(user as Worker)
        }
        editor.putString(userKey, userJson)
        editor.putBoolean(userType, user.isClient ?: true)
        editor.apply()
    }

    fun clearUser() {
        val editor = sharedPreferences.edit()
        editor.remove(userKey)
        editor.remove(userType)
        editor.apply()
    }

    fun addTable(table: Table) {
        val userJson = sharedPreferences.getString(userKey, null)
        val userType = sharedPreferences.getBoolean(userType, true)
        if (!userType) {
            val worker = Gson().fromJson(userJson, Worker::class.java)
            worker.restaurant?.tables?.add(table)
            saveUser(worker)
        }
    }

    fun addRestaurant(restaurant: Restaurant) {
        val userJson = sharedPreferences.getString(userKey, null)
        val userType = sharedPreferences.getBoolean(userType, true)
        if (!userType) {
            val worker = Gson().fromJson(userJson, Worker::class.java)
            worker.isOwner = true
            worker.restaurant = restaurant
            saveUser(worker)
        }
    }
}