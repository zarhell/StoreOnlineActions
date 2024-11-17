package com.storeonline.infrastructure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.application.adapter.CartAdapter
import com.storeonline.databinding.ActivityPurchaseBinding
import com.storeonline.infrastructure.repository.CartRepository

class PurchaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchaseBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cartItems = CartRepository.getItems()

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "No hay productos seleccionados.", Toast.LENGTH_SHORT).show()
            finish()
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
                    if (CartRepository.isCartEmpty()) {
                        Toast.makeText(this, "El carrito está vacío.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            )
            binding.recyclerViewPurchase.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewPurchase.adapter = cartAdapter
        }

        binding.btnConfirmPurchase.setOnClickListener {
            if (CartRepository.isCartEmpty()) {
                Toast.makeText(this, "No hay productos en el carrito para comprar.", Toast.LENGTH_SHORT).show()
            } else {
                navigateToPayment()
            }
        }
    }

    private fun navigateToPayment() {
        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
        finish()
    }
}
