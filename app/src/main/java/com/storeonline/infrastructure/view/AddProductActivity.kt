package com.storeonline.infrastructure.view

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.storeonline.databinding.ActivityAddProductBinding
import com.storeonline.domain.model.ProductContract
import com.storeonline.infrastructure.repository.ProductDbRepository

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = ProductDbRepository(this)
        database = dbHelper.writableDatabase

        binding.btnAddProduct.setOnClickListener {
            val productName = binding.etProductName.text.toString()
            val productPrice = binding.etProductPrice.text.toString().toDoubleOrNull()

            if (productName.isNotEmpty() && productPrice != null) {
                addProductToDb(productName, productPrice)
                Toast.makeText(this, "Producto agregado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor, ingrese detalles válidos del producto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addProductToDb(name: String, price: Double) {
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.COLUMN_NAME_NAME, name)
            put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, price)
        }
        database.insert(ProductContract.ProductEntry.TABLE_NAME, null, values)
    }
}