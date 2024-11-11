package com.storeonline.infrastructure.repository

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.storeonline.domain.model.ProductContract

class ProductDbRepository(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ProductContract.ProductEntry.SQL_CREATE_ENTRIES);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(ProductContract.ProductEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    companion object {
        const val DATABASE_VERSION = 4;
        const val DATABASE_NAME = "StoreOnline.db";
    }
}
