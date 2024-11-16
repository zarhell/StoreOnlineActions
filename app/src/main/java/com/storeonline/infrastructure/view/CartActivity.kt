package com.storeonline.infrastructure.view

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.storeonline.application.adapter.CartAdapter
import com.storeonline.databinding.ActivityCartBinding
import com.storeonline.domain.model.Product
import com.storeonline.application.client.CartContract
import com.storeonline.infrastructure.repository.CartRepository
import com.storeonline.infrastructure.repository.ProductDbRepository

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter    : CartAdapter
    private lateinit var database: SQLiteDatabase
    private var selectedProducts: ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = ProductDbRepository(this)
        database = dbHelper.readableDatabase

        loadCartProducts()

        if (selectedProducts.isEmpty()) {
            Toast.makeText(this, "No hay productos seleccionados.", Toast.LENGTH_SHORT).show()
        } else {
            cartAdapter = CartAdapter(
                cartItems = CartRepository.getItems(),
                onQuantityChange = { cartItem, newQuantity ->
                    CartRepository.updateQuantity(cartItem.product.id, newQuantity)
                    cartAdapter.notifyDataSetChanged()
                },
                onRemove = { cartItem ->
                    CartRepository.removeItem(cartItem.product.id)
                    cartAdapter.notifyDataSetChanged()
                }
            )
            binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
            binding.recyclerViewCart.adapter = cartAdapter
        }

        binding.btnProceedToPurchase.setOnClickListener {
            val intent = Intent(this, PurchaseActivity::class.java)
            intent.putParcelableArrayListExtra("selectedProducts", selectedProducts)
            startActivity(intent)
        }
    }

    private fun loadCartProducts() {
        val cartItems = CartRepository.getItems()

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "No hay productos seleccionados.", Toast.LENGTH_SHORT).show()
        } else {
            cartAdapter = CartAdapter(cartItems,
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
