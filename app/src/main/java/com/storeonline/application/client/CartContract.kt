package com.storeonline.application.client

import android.provider.BaseColumns

object CartContract {
    object CartEntry : BaseColumns {
        const val TABLE_NAME = "cart"
        const val COLUMN_NAME_PRODUCT_ID = "product_id"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_PRICE = "price"
        const val COLUMN_NAME_IMAGE = "image"
    }
}
