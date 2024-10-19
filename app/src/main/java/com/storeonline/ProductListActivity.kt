package com.storeonline

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.databinding.ActivityProductListBinding

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private val selectedProducts = mutableListOf<Product>() // Cambiar a Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crear una lista de productos
        val products = listOf(
            Product("Product 1", 10.99),
            Product("Product 2", 15.99),
            Product("Product 3", 7.49)
        )

        // Configurar el RecyclerView con ProductAdapter
        val productAdapter = ProductAdapter(products) { product ->
            // Agregar el producto seleccionado a la lista
            selectedProducts.add(product)
        }

        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(this@ProductListActivity)
            adapter = productAdapter
        }

        // Manejar el clic en el botón "Go to Cart"
        binding.btnGoToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putParcelableArrayListExtra("selectedProducts", ArrayList(selectedProducts))
            startActivity(intent)
        }
    }
}
