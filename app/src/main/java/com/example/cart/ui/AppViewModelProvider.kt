
package com.example.cart.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cart.CartApplication
import com.example.cart.ui.home.HomeViewModel
import com.example.cart.ui.detail.DetailDetailsViewModel
import com.example.cart.ui.detail.DetailEntryViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            DetailEntryViewModel(cartApplication().container.cartsRepository)
        }

        // Initializer for DetailDetailsViewModel
        initializer {
            DetailDetailsViewModel(
                this.createSavedStateHandle(),
                cartApplication().container.cartsRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(cartApplication().container.cartsRepository)
        }
    }
}
fun CreationExtras.cartApplication(): CartApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as CartApplication)
