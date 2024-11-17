package com.storeonline.infrastructure.repository

import android.content.ContentValues
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast
import com.storeonline.application.client.ProductContract
import com.storeonline.domain.model.Product

class ProductDbRepository(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ProductContract.ProductEntry.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(ProductContract.ProductEntry.SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "storeonline.db"
        private const val DATABASE_VERSION = 9
    }

    fun addProduct(product: Product) {
        val imagesString: String = product.images.joinToString(",")
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.COLUMN_NAME_NAME, product.name)
            put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, product.price)
            put(ProductContract.ProductEntry.COLUMN_NAME_IMAGES, imagesString)
        }
        writableDatabase.insert(ProductContract.ProductEntry.TABLE_NAME, null, values)
    }

    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val cursor = readableDatabase.query(
            ProductContract.ProductEntry.TABLE_NAME,
            null, null, null, null, null, null
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_ID))
                val name = it.getString(it.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_NAME))
                val price = it.getDouble(it.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_PRICE))
                val imagesString = it.getString(it.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_IMAGES))

                val images = if (imagesString.isNullOrEmpty()) {
                    emptyList()
                } else {
                    imagesString.split(",").map { it.trim() }
                }

                products.add(Product(id, name, price, images))
            }
        }
        return products
    }

    fun addToCart(productId: Long, quantity: Int) {
        val values = ContentValues().apply {
            put("product_id", productId)
            put("quantity", quantity)
        }
        try {
            writableDatabase.insert("Cart", null, values)
        } catch (e: Exception) {
            Toast.makeText(
                null,
                "Error al agregar producto al carrito: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
