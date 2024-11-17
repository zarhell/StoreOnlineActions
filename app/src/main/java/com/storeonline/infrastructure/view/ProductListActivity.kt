package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.application.adapter.ProductAdapter
import com.storeonline.databinding.ActivityProductListBinding
import com.storeonline.domain.model.Product
import com.storeonline.domain.service.ProductService
import com.storeonline.infrastructure.repository.CartRepository
import com.storeonline.infrastructure.utils.ProductUtils

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productAdapter: ProductAdapter
    private var allProducts: List<Product> = emptyList()
    private lateinit var productService: ProductService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        initializeProductService()
        preloadProducts()
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

    private fun initializeProductService() {
        productService = ProductService(this)

    }


    private fun preloadProducts() {
        val defaultProducts = listOf(
            Product(0, "Laptop", 1500.0, ProductUtils.getLaptopImages()),
            Product(0, "Smartphone", 800.0, ProductUtils.getSmartphoneImages()),
            Product(0, "Headphones", 200.0, ProductUtils.getHeadphonesImages())
        )
        productService.preloadDefaultProducts(defaultProducts)
        allProducts = productService.getAllProducts()
    }

    private fun setupToolbar() {
        val headerToolbar: Toolbar = binding.toolbar
        setSupportActionBar(headerToolbar)
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(allProducts) { product ->
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

    private fun setupCartButton() {
        binding.btnGoToCart.setOnClickListener {
            if (CartRepository.isCartEmpty()) {
                Toast.makeText(this, "El carrito está vacío.", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
    }

    private fun reloadProducts() {
        allProducts = productService.getAllProducts()
        productAdapter.updateList(allProducts)
    }

    private fun navigateToProductDetail(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("selectedProduct", product)
        startActivity(intent)
    }

    private fun addToCart(product: Product) {
        CartRepository.addItem(product)
        Toast.makeText(this, "${product.name} agregado al carrito.", Toast.LENGTH_SHORT).show()
    }

    private fun filterProducts(query: String?) {
        val filteredProducts = if (query.isNullOrEmpty()) {
            allProducts
        } else {
            allProducts.filter { it.name.contains(query, ignoreCase = true) }
        }
        productAdapter.updateList(filteredProducts)
    }
}
