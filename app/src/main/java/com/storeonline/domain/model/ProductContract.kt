package com.storeonline.domain.model

import android.provider.BaseColumns;

object ProductContract {
    object ProductEntry : BaseColumns {
        const val TABLE_NAME = "product";

        const val COLUMN_NAME_ID = BaseColumns._ID;
        const val COLUMN_NAME_NAME = "name";
        const val COLUMN_NAME_PRICE = "price";
        const val COLUMN_NAME_IMAGE = "image";

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${TABLE_NAME} (${COLUMN_NAME_ID} INTEGER PRIMARY KEY, ${COLUMN_NAME_NAME} TEXT, ${COLUMN_NAME_PRICE} REAL, ${COLUMN_NAME_IMAGE} TEXT)";

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}";
    }
}
