package com.storeonline.domain.service

import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import com.storeonline.domain.model.User

class AccountService(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    private val sessionPrefs: SharedPreferences = context.getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)

    private val SESSION_DURATION = 30 * 60 * 1000L

    fun registerUser(user: User): Boolean {
        val editor = sharedPreferences.edit()
        editor.putString(user.username, user.password)
        return editor.commit()
    }

    fun authenticateUser(username: String, password: String): Boolean {
        val storedPassword = sharedPreferences.getString(username, null)
        if (storedPassword != null && storedPassword == password) {
            loginUser(username)
            return true
        }
        return false
    }

    private fun loginUser(username: String) {
        val editor = sessionPrefs.edit()
        editor.putString("activeUser", username)
        editor.putLong("sessionStartTime", SystemClock.elapsedRealtime())
        editor.apply()
    }
    fun isSessionActive(): Boolean {
        val sessionStartTime = sessionPrefs.getLong("sessionStartTime", -1L)
        return if (sessionStartTime != -1L) {
            val currentTime = SystemClock.elapsedRealtime()
            val elapsedTime = currentTime - sessionStartTime
            if (elapsedTime < SESSION_DURATION) {
                true
            } else {
                logoutUser()
                false
            }
        } else {
            false
        }
    }

    fun deleteUser(username: String): Boolean {
        if (sharedPreferences.contains(username)) {
            sharedPreferences.edit().remove(username).apply()
            return true
        }
        return false
    }

    fun logoutUser() {
        sharedPreferences.edit().clear().apply()
    }
}
