package com.example.cart.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cart.data.Cart
import com.example.cart.data.CartsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val cartsRepository: CartsRepository) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        cartsRepository.getAllCartsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    init {
        insertInitialData()
    }

    private fun insertInitialData() {
        viewModelScope.launch {
            val initialDetails = listOf(
                Cart(id = 1, userId = "user1", cartId = "cart1", name = "Sample Cart 1", nproducts = "5", status = "Pending", total = 99.99),
                Cart(id = 2, userId = "user2", cartId = "cart2", name = "Sample Cart 2", nproducts = "3", status = "Completed", total = 49.99),
                Cart(id = 3, userId = "user3", cartId = "cart3", name = "Sample Cart 3", nproducts = "8", status = "Pending", total = 149.99)
            )
            initialDetails.forEach { cartsRepository.insertDetail(it) }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val detailList: List<Cart> = listOf())
