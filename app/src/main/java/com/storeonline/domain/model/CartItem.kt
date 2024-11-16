package com.storeonline.domain.model

data class CartItem(
    val product: Product,
    var quantity: Int
)