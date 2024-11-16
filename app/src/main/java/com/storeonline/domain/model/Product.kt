package com.storeonline.domain.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val images: List<String>
) : Parcelable

