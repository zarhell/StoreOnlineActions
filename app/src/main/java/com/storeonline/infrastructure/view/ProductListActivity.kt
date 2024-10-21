package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.application.adapter.ProductAdapter
import com.storeonline.databinding.ActivityProductListBinding
import com.storeonline.domain.model.Product
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var allProducts: List<Product>
    private val selectedProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val headerToolbar: Toolbar = binding.toolbar
        setSupportActionBar(headerToolbar)

        allProducts = listOf(
            Product("Product 1", 10.99),
            Product("Product 2", 15.99),
            Product("Product 3", 7.49),
            Product("Laptop", 1000.0),
            Product("Phone", 500.0)
        )

        productAdapter = ProductAdapter(allProducts) { product ->
            selectedProducts.add(product)
        }
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = productAdapter

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


        binding.btnGoToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putParcelableArrayListExtra("selectedProducts", ArrayList(selectedProducts))
            startActivity(intent)
        }
    }
}
