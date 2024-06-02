package com.example.cart.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carts")
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val cartId: String,
    val name: String,
    val nproducts: String,
    val status: String,
    val total: Double,
    )
