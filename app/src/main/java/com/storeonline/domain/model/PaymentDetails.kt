package com.storeonline.domain.model

data class PaymentDetails(
    val cardNumber: String,
    val cardholderName: String,
    val expiryDate: String,
    val cvv: String,
    val shippingAddress: String,
    val city: String,
    val postalCode: String,
    val country: String
)
