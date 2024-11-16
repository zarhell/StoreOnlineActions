package com.storeonline.infrastructure.repository

import com.storeonline.domain.model.CartItem
import com.storeonline.domain.model.Product

object CartRepository {
    private val cartItems = mutableListOf<CartItem>()

    fun addItem(product: Product, quantity: Int = 1) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            cartItems.add(CartItem(product, quantity))
        }
    }

    fun removeItem(productId: Long) {
        cartItems.removeAll { it.product.id == productId }
    }

    fun updateQuantity(productId: Long, newQuantity: Int) {
        val item = cartItems.find { it.product.id == productId }
        if (item != null) {
            if (newQuantity > 0) {
                item.quantity = newQuantity
            } else {
                removeItem(productId)
            }
        }
    }

    fun getItems(): List<CartItem> = cartItems

    fun clearCart() {
        cartItems.clear()
    }

    fun isCartEmpty(): Boolean = cartItems.isEmpty()
}
