package com.storeonline.infrastructure.view

import android.annotation.SuppressLint
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
import com.storeonline.infrastructure.repository.CartRepository
import com.storeonline.infrastructure.repository.ProductDbRepository

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var allProducts: List<Product>
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        initializeDatabase()
        preloadProductsIfNeeded()
        setupToolbar()
        setupRecyclerView()
        setupSearchView()
        setupCartButton()
    }

    override fun onResume() {
        super.onResume()
        reloadProducts()
    }

    private fun setupView() {
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initializeDatabase() {
        val dbHelper = ProductDbRepository(this)
        database = dbHelper.writableDatabase
    }

    private fun setupToolbar() {
        val headerToolbar: Toolbar = binding.toolbar
        setSupportActionBar(headerToolbar)
    }

    private fun preloadProductsIfNeeded() {
        if (getAllProductsFromDb().isEmpty()) {
            val defaultProducts = listOf(
                Product(0, "Laptop", 1500.0, getLaptopImages()),
                Product(0, "Smartphone", 800.0, getSmartphoneImages()),
                Product(0, "Headphones", 200.0, getHeadphonesImages())
            )
            defaultProducts.forEach { addProductToDb(it) }
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList()) { product ->
            navigateToProductDetail(product)
        }
        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(this@ProductListActivity)
            adapter = productAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts(newText)
                return true
            }
        })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setupCartButton() {
        binding.btnGoToCart.setOnClickListener {
            openCart()
        }
    }

    private fun reloadProducts() {
        allProducts = getAllProductsFromDb()
        productAdapter.updateList(allProducts)
    }

    private fun navigateToProductDetail(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("selectedProduct", product)
        startActivity(intent)
        CartRepository.addItem(product)
        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
    }

    private fun filterProducts(query: String?) {
        val filteredProducts = if (query.isNullOrEmpty()) {
            allProducts
        } else {
            allProducts.filter { it.name.contains(query, ignoreCase = true) }
        }
        productAdapter.updateList(filteredProducts)
    }

    private fun openCart() {
        if (CartRepository.isCartEmpty()) {
            showToast("El carrito está vacío.")
        } else {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addProductToDb(product: Product) {
        if (!isProductInDb(product.name)) {
            val values = ContentValues().apply {
                put(ProductContract.ProductEntry.COLUMN_NAME_NAME, product.name)
                put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, product.price)
                put(
                    ProductContract.ProductEntry.COLUMN_NAME_IMAGES,
                    product.images.joinToString(",")
                )
            }
            database.insert(ProductContract.ProductEntry.TABLE_NAME, null, values)
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
                productList.add(cursorToProduct(it))
            }
        }
        return productList
    }

    private fun getSelectedProductsFromDb(): List<Product> {
        val selectedProducts = mutableListOf<Product>()

        val cartCursor = database.query(
            "Cart",
            arrayOf("product_id", "quantity"),
            null,
            null,
            null,
            null,
            null
        )

        cartCursor.use {
            while (it.moveToNext()) {
                val productId = it.getLong(it.getColumnIndexOrThrow("product_id"))
                val quantity = it.getInt(it.getColumnIndexOrThrow("quantity"))
                val product = getProductById(productId)
                repeat(quantity) { selectedProducts.add(product) }
            }
        }
        return selectedProducts
    }

    private fun getProductById(productId: Long): Product {
        val cursor = database.query(
            ProductContract.ProductEntry.TABLE_NAME,
            null,
            "${ProductContract.ProductEntry.COLUMN_NAME_ID} = ?",
            arrayOf(productId.toString()),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return cursorToProduct(it)
            }
        }
        throw Exception("Producto no encontrado.")
    }

    private fun cursorToProduct(cursor: android.database.Cursor): Product {
        val id =
            cursor.getLong(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_ID))
        val name =
            cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_NAME))
        val price =
            cursor.getDouble(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_PRICE))
        val images =
            cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_IMAGES))
                .split(",")
        return Product(id, name, price, images)
    }

    private fun isProductInDb(productName: String): Boolean {
        val cursor = database.query(
            ProductContract.ProductEntry.TABLE_NAME,
            null,
            "${ProductContract.ProductEntry.COLUMN_NAME_NAME} = ?",
            arrayOf(productName),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun getLaptopImages() = listOf(
        "https://acortar.link/712Sz8",
        "https://www.freepik.es/foto-gratis/arreglo-lapices-laptops_13659290.htm",
        "https://img.freepik.com/foto-gratis/cerrar-mujer-usando-laptop_23-2148860629.jpg"
    )

    private fun getSmartphoneImages() = listOf(
        "https://acortar.link/6ou26d",
        "https://img.freepik.com/foto-gratis/elegante-composicion-telefono-inteligente_23-2149437106.jpg",
        "https://img.freepik.com/vector-gratis/telefono-fondo-degradado_23-2147848028.jpg"
    )

    private fun getHeadphonesImages() = listOf(
        "https://acortar.link/hc3rwU",
        "https://img.freepik.com/foto-gratis/vida-muerta-auriculares-ciberneticos-inalambricos_23-2151072206.jpg",
        "https://img.freepik.com/foto-gratis/vista-aerea-auriculares-blancos-sobre-fondo-azul_23-2147889912.jpg"
    )
}
