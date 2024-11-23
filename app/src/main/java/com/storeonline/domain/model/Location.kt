package com.storeonline.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val id: Int = 0,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val estimatedDelivery: String
) : Parcelable
