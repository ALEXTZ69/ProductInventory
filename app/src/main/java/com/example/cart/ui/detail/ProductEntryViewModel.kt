/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cart.ui.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cart.data.Cart
import com.example.cart.data.CartsRepository
import java.text.NumberFormat

class DetailEntryViewModel(private val cartsRepository: CartsRepository) : ViewModel() {

    var detailUiState by mutableStateOf(DetailUiState())
        private set

    fun updateUiState(detailDetails: DetailDetails) {
        detailUiState =
            DetailUiState(detailDetails = detailDetails, isEntryValid = validateInput(detailDetails))
    }

    suspend fun saveDetail() {
        if (validateInput()) {
            cartsRepository.insertDetail(detailUiState.detailDetails.toDetail())
        }
    }

    private fun validateInput(uiState: DetailDetails = detailUiState.detailDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && status.isNotBlank() && category.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Detail.
 */
data class DetailUiState(
    val detailDetails: DetailDetails = DetailDetails(),
    val isEntryValid: Boolean = false
)

data class DetailDetails(
    val id: Int = 0,
    val userId: String = "",
    val name: String = "",
    val cartId: String = "",
    val status: String = "",
    val category: String = "",
    val nproducts: String = "",
    val total: String = "",
)

fun DetailDetails.toDetail(): Cart = Cart(
    id = id,
    userId = userId,
    name = name,
    cartId = cartId,
    status = status,
    nproducts = nproducts,
    total = total.toDoubleOrNull() ?: 0.0,
)

fun Cart.formatedTotal(): String {
    return NumberFormat.getCurrencyInstance().format(total)
}


fun Cart.toDetailUiState(isEntryValid: Boolean = false): DetailUiState = DetailUiState(
    detailDetails = this.toDetailDetails(),
    isEntryValid = isEntryValid
)

fun Cart.toDetailDetails(): DetailDetails = DetailDetails(
    id = id,
    userId = userId,
    name = name,
    cartId = cartId,
    status = status,
    nproducts = nproducts,
    total = total.toString(),
)
