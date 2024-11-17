package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.application.adapter.CartAdapter
import com.storeonline.databinding.ActivityCartBinding
import com.storeonline.infrastructure.repository.CartRepository

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCartProducts()

        binding.btnProceedToPurchase.setOnClickListener {
            if (!CartRepository.isCartEmpty()) {
                val intent = Intent(this, ShippingDetailsActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCartProducts() {
        val cartItems = CartRepository.getItems()

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "No hay productos seleccionados.", Toast.LENGTH_SHORT).show()
        } else {
            cartAdapter = CartAdapter(
                cartItems = cartItems,
                onQuantityChange = { cartItem, newQuantity ->
                    CartRepository.updateQuantity(cartItem.product.id, newQuantity)
                    cartAdapter.notifyDataSetChanged()
                },
                onRemove = { cartItem ->
                    CartRepository.removeItem(cartItem.product.id)
                    cartAdapter.notifyDataSetChanged()
                })
            binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewCart.adapter = cartAdapter
        }
    }
}
