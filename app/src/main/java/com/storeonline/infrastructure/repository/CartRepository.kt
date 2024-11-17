package com.storeonline.infrastructure.repository

import android.annotation.SuppressLint
import com.storeonline.domain.model.CartItem
import com.storeonline.domain.model.Product

object CartRepository {
    private val cartItems = mutableListOf<CartItem>()

    fun addItem(product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }

    fun getItems(): List<CartItem> = cartItems

    fun updateQuantity(productId: Long, quantity: Int) {
        cartItems.find { it.product.id == productId }?.apply {
            this.quantity = quantity
        }
    }

    @SuppressLint("NewApi")
    fun removeItem(productId: Long) {
        cartItems.removeIf { it.product.id == productId }
    }

    fun isCartEmpty(): Boolean = cartItems.isEmpty()
}
