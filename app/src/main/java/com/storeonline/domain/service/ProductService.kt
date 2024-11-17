package com.storeonline.domain.service

import android.content.Context
import com.storeonline.domain.model.Product
import com.storeonline.infrastructure.repository.ProductDbRepository

class ProductService(context: Context) {
    private val productRepository: ProductDbRepository = ProductDbRepository(context)

    fun addProduct(product: Product) {
        productRepository.addProduct(product)
    }

    fun getAllProducts(): List<Product> {
        return productRepository.getAllProducts()
    }

    fun hasProducts(): Boolean {
        return getAllProducts().isNotEmpty()
    }

    fun addToCart(productId: Long, quantity: Int) {
        productRepository.addToCart(productId, quantity)
    }

    fun preloadDefaultProducts(defaultProducts: List<Product>) {
        if (!hasProducts()) {
            defaultProducts.forEach { addProduct(it) }
        }
    }
}
