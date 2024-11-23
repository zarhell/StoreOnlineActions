package com.storeonline.infrastructure.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.R
import com.storeonline.application.adapter.ProductImageAdapter
import com.storeonline.databinding.ActivityProductDetailBinding
import com.storeonline.domain.model.Product
import com.storeonline.infrastructure.repository.CartRepository

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private var product: Product? = null


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        product = intent.getParcelableExtra("selectedProduct", Product::class.java)

        if (product != null) {
            displayProductDetails(product!!)
            setupAddToCartButton(product!!)
        } else {
            Toast.makeText(this, "Error al cargar el producto.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun displayProductDetails(product: Product) {
        binding.productName.text = product.name
        binding.productPrice.text = getString(R.string.product_price_format, product.price)

        val imageAdapter = ProductImageAdapter(product.images)
        binding.recyclerViewProductImages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewProductImages.adapter = imageAdapter

        binding.productFeatures.text = "Características del producto:\n- Detalle 1\n- Detalle 2\n- Detalle 3"
    }

    private fun setupAddToCartButton(product: Product) {
        binding.btnAddToCart.setOnClickListener {
            CartRepository.addItem(product)
            Toast.makeText(this, "${product.name} agregado al carrito.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
