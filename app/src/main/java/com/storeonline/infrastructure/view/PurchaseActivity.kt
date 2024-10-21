package com.storeonline.infrastructure.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.application.adapter.CartAdapter
import com.storeonline.databinding.ActivityPurchaseBinding
import com.storeonline.domain.model.Product
import com.storeonline.infrastructure.utils.parcelableArrayList

class PurchaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchaseBinding
    private lateinit var cartAdapter: CartAdapter
    private var selectedProducts: ArrayList<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedProducts = intent.parcelableArrayList("selectedProducts")

        if (selectedProducts.isNullOrEmpty()) {
            Toast.makeText(this, "No hay productos seleccionados.", Toast.LENGTH_SHORT).show()
        } else {
            cartAdapter = CartAdapter(selectedProducts!!)
            binding.recyclerViewPurchase.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewPurchase.adapter = cartAdapter
        }

        binding.btnConfirmPurchase.setOnClickListener {
            Toast.makeText(this, "Compra confirmada!", Toast.LENGTH_SHORT).show()
        }
    }
}
