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

package com.example.cart

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cart.data.CartDatabase
import com.example.cart.data.Cart
import com.example.cart.data.CartDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DetailDaoTest {

    private lateinit var detailDao: CartDao
    private lateinit var inventoryDatabase: CartDatabase
    private val detail1 = Cart(1, "Apples", "1.0", "fruit", "", "",0.50)
    private val detail2 = Cart(2, "Bananas", "50", "fruit","", "",0.50)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, CartDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        detailDao = inventoryDatabase.detailDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsDetailIntoDB() = runBlocking {
        addOneDetailToDb()
        val allCarts = detailDao.getAllCarts().first()
        assertEquals(allCarts[0], detail1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllCarts_returnsAllCartsFromDB() = runBlocking {
        addTwoCartsToDb()
        val allCarts = detailDao.getAllCarts().first()
        assertEquals(allCarts[0], detail1)
        assertEquals(allCarts[1], detail2)
    }


    @Test
    @Throws(Exception::class)
    fun daoGetDetail_returnsDetailFromDB() = runBlocking {
        addOneDetailToDb()
        val detail = detailDao.getDetail(1)
        assertEquals(detail.first(), detail1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteCarts_deletesAllCartsFromDB() = runBlocking {
        addTwoCartsToDb()
        detailDao.delete(detail1)
        detailDao.delete(detail2)
        val allCarts = detailDao.getAllCarts().first()
        assertTrue(allCarts.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateCarts_updatesCartsInDB() = runBlocking {
        addTwoCartsToDb()
        detailDao.update(Cart(1, "Apples", "1.0", "fruit", "", "",0.50))
        detailDao.update(Cart(2, "Bananas", "50", "fruit","", "",0.50))

        val allCarts = detailDao.getAllCarts().first()
        assertEquals(allCarts[0], Cart(1, "Apples", "1.0", "fruit", "", "",0.50))
        assertEquals(allCarts[1], Cart(2, "Bananas", "50", "fruit","", "",0.50))
    }

    private suspend fun addOneDetailToDb() {
        detailDao.insert(detail1)
    }

    private suspend fun addTwoCartsToDb() {
        detailDao.insert(detail1)
        detailDao.insert(detail2)
    }
}
