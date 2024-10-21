package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.application.adapter.CartAdapter
import com.storeonline.domain.model.Product
import com.storeonline.databinding.ActivityCartBinding
import com.storeonline.infrastructure.utils.parcelableArrayList

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter    : CartAdapter
    private var selectedProducts: ArrayList<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedProducts = intent.parcelableArrayList("selectedProducts")

        if (selectedProducts.isNullOrEmpty()) {
            Toast.makeText(this, "No hay productos seleccionados.", Toast.LENGTH_SHORT).show()
        } else {
            cartAdapter = CartAdapter(selectedProducts!!)
            binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewCart.adapter = cartAdapter
        }

        // Configurar el botón para proceder a la compra
        binding.btnProceedToPurchase.setOnClickListener {
            val intent = Intent(this, PurchaseActivity::class.java)
            intent.putParcelableArrayListExtra("selectedProducts", selectedProducts)
            startActivity(intent)
        }
    }
}
