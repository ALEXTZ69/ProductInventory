package com.example.cart.data

import android.content.Context

interface AppContainer {
    val cartsRepository: CartsRepository
}

class AppDataContainer(context: Context) : AppContainer {
    private val database = CartDatabase.getDatabase(context)
    override val cartsRepository: CartsRepository by lazy {
        DefaultCartsRepository(database.detailDao())
    }
}
