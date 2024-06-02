package com.example.cart.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cart.data.CartsRepository
import com.example.cart.data.Cart
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val cartsRepository: CartsRepository,
) : ViewModel() {

    private val detailId: Int = checkNotNull(savedStateHandle[DetailDetailsDestination.detailIdArg])

    val uiState: StateFlow<DetailDetailsUiState> =
        cartsRepository.getDetailStream(detailId)
            .filterNotNull()
            .map {
                DetailDetailsUiState(outOfStock = it.name <= "", detailDetails = it.toDetailDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetailDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class DetailDetailsUiState(
    val outOfStock: Boolean = true,
    val detailDetails: DetailDetails = DetailDetails()
)
