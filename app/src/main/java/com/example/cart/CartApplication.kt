package com.example.cart

import android.app.Application
import com.example.cart.data.AppContainer
import com.example.cart.data.AppDataContainer

class CartApplication : Application() {


    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
