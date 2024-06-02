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

package com.example.cart.data

import kotlinx.coroutines.flow.Flow


interface CartsRepository {

    fun getAllCartsStream(): Flow<List<Cart>>

    fun getDetailStream(id: Int): Flow<Cart?>

    suspend fun insertDetail(detail: Cart)

    suspend fun deleteDetail(detail: Cart)

    suspend fun updateDetail(detail: Cart)
}

class DefaultCartsRepository(private val detailDao: CartDao) : CartsRepository {

    override fun getAllCartsStream(): Flow<List<Cart>> = detailDao.getAllCarts()

    override fun getDetailStream(id: Int): Flow<Cart?> = detailDao.getDetail(id)

    override suspend fun insertDetail(detail: Cart) = detailDao.insert(detail)

    override suspend fun deleteDetail(detail: Cart) = detailDao.delete(detail)

    override suspend fun updateDetail(detail: Cart) = detailDao.update(detail)
}