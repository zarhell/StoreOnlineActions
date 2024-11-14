package com.storeonline.infrastructure.view

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.application.adapter.ProductAdapter
import com.storeonline.databinding.ActivityProductListBinding
import com.storeonline.domain.model.Product
import com.storeonline.application.client.ProductContract
import com.storeonline.infrastructure.repository.ProductDbRepository

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var allProducts: List<Product>
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityProductListBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al inflar el layout: \${e.message}", Toast.LENGTH_LONG).show()
            return
        }

        val dbHelper = ProductDbRepository(this)
        database = dbHelper.writableDatabase

        val headerToolbar: Toolbar = binding.toolbar
        setSupportActionBar(headerToolbar)

        preloadProducts()
        loadProducts()
        setupRecyclerView()
        setupSearchView()
        setupCartButton()
    }

    override fun onResume() {
        super.onResume()

        loadProducts()
        productAdapter.updateList(allProducts)
    }

    private fun preloadProducts() {
        try {
            if (getAllProductsFromDb().isEmpty()) {
                val productsToAdd = listOf(
                    Product(1, "Laptop", 1500.0, "https://acortar.link/712Sz8"),
                    Product(2, "Smartphone", 800.0, "https://acortar.link/6ou26d"),
                    Product(3, "Headphones", 200.0, "https://acortar.link/hc3rwU")
                )

                productsToAdd.forEach { product ->
                    addProductToDb(product)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al precargar productos: \${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun addProductToDb(product: Product) {
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.COLUMN_NAME_NAME, product.name)
            put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, product.price)
            put(ProductContract.ProductEntry.COLUMN_NAME_IMAGE, product.image)
        }
        try {
            database.insert(ProductContract.ProductEntry.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al agregar producto a la base de datos: \${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadProducts() {
        try {
            allProducts = getAllProductsFromDb()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar productos: \${e.message}", Toast.LENGTH_LONG).show()
            allProducts = emptyList()
        }
    }

    private fun setupRecyclerView() {
        if (!::allProducts.isInitialized) {
            allProducts = emptyList()
        }

        productAdapter = ProductAdapter(allProducts) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("selectedProduct", product)
            startActivity(intent)
        }
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = productAdapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredProducts = if (newText.isNullOrEmpty()) {
                    allProducts
                } else {
                    allProducts.filter { product ->
                        product.name.contains(newText, ignoreCase = true)
                    }
                }
                productAdapter.updateList(filteredProducts)
                return true
            }
        })
    }

    private fun setupCartButton() {
        binding.btnGoToCart.setOnClickListener {
            try {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Error al abrir el carrito de compras: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getAllProductsFromDb(): List<Product> {
        val productList = mutableListOf<Product>()
        val cursor = database.query(
            ProductContract.ProductEntry.TABLE_NAME,
            null, null, null, null, null, null
        )

        cursor.use {
            while (it.moveToNext()) {
                try {
                    val id = it.getLong(it.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_ID))
                    val name = it.getString(it.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_NAME))
                    val price = it.getDouble(it.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_PRICE))
                    val imagePath = it.getString(it.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_IMAGE))
                    productList.add(Product(id, name, price, imagePath))
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al leer producto: \${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        return productList
    }

    private fun deleteProductFromDb(product: Product) {
        val selection = "${ProductContract.ProductEntry.COLUMN_NAME_ID} = ?"
        val selectionArgs = arrayOf(product.id.toString())
        try {
            database.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al eliminar producto de la base de datos: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}