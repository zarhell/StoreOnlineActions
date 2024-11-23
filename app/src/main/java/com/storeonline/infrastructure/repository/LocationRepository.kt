package com.storeonline.infrastructure.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.storeonline.domain.model.Location

class LocationRepository(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TABLE)
        onCreate(db)
    }

    fun saveLocation(location: Location): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADDRESS, location.address)
            put(COLUMN_LATITUDE, location.latitude)
            put(COLUMN_LONGITUDE, location.longitude)
            put(COLUMN_ESTIMATED_DELIVERY, location.estimatedDelivery)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    companion object {
        private const val DATABASE_NAME = "storeonline.db"
        private const val DATABASE_VERSION = 9
        private const val TABLE_NAME = "locations"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
        private const val COLUMN_ESTIMATED_DELIVERY = "estimated_delivery"

        private const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_ADDRESS TEXT NOT NULL, " +
                    "$COLUMN_LATITUDE REAL NOT NULL, " +
                    "$COLUMN_LONGITUDE REAL NOT NULL, " +
                    "$COLUMN_ESTIMATED_DELIVERY TEXT NOT NULL)"

        private const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
