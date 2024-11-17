package com.storeonline.infrastructure.repository

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.SystemClock
import com.storeonline.application.client.UserContract
import com.storeonline.domain.model.User

class AccountRepository(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    private val sessionPrefs: SharedPreferences =
        context.getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)

    private val SESSION_DURATION = 30 * 60 * 1000L

    companion object {
        private const val DATABASE_NAME = "storeonline.db"
        private const val DATABASE_VERSION = 9
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE ${UserContract.UserEntry.TABLE_NAME} (" +
                "${UserContract.UserEntry.COLUMN_NAME_USERNAME} TEXT PRIMARY KEY," +
                "${UserContract.UserEntry.COLUMN_NAME_PASSWORD} TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${UserContract.UserEntry.TABLE_NAME}")
        onCreate(db)
    }

    fun registerUser(user: User): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(UserContract.UserEntry.COLUMN_NAME_USERNAME, user.username)
                put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, user.password)
            }
            val newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values)
            newRowId != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun authenticateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        var cursor: Cursor? = null
        return try {
            cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                arrayOf(
                    UserContract.UserEntry.COLUMN_NAME_USERNAME,
                    UserContract.UserEntry.COLUMN_NAME_PASSWORD
                ),
                "${UserContract.UserEntry.COLUMN_NAME_USERNAME} = ? AND ${UserContract.UserEntry.COLUMN_NAME_PASSWORD} = ?",
                arrayOf(username, password),
                null,
                null,
                null
            )
            cursor.count > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            cursor?.close()
        }
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

    fun doesUserExist(username: String): Boolean {
        val db = readableDatabase
        var cursor: Cursor? = null
        return try {
            cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                arrayOf(UserContract.UserEntry.COLUMN_NAME_USERNAME),
                "${UserContract.UserEntry.COLUMN_NAME_USERNAME} = ?",
                arrayOf(username),
                null,
                null,
                null
            )
            cursor.count > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            cursor?.close()
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
