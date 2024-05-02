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

package com.example.inventory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Product
import com.example.inventory.data.ProductDao
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
class ProductDaoTest {

    private lateinit var productDao: ProductDao
    private lateinit var inventoryDatabase: InventoryDatabase
    private val product1 = Product(1, "Apples", 1.0, "fruit")
    private val product2 = Product(2, "Bananas", 0.50, "fruit")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        productDao = inventoryDatabase.productDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsProductIntoDB() = runBlocking {
        addOneProductToDb()
        val allProducts = productDao.getAllProducts().first()
        assertEquals(allProducts[0], product1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllProducts_returnsAllProductsFromDB() = runBlocking {
        addTwoProductsToDb()
        val allProducts = productDao.getAllProducts().first()
        assertEquals(allProducts[0], product1)
        assertEquals(allProducts[1], product2)
    }


    @Test
    @Throws(Exception::class)
    fun daoGetProduct_returnsProductFromDB() = runBlocking {
        addOneProductToDb()
        val product = productDao.getProduct(1)
        assertEquals(product.first(), product1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteProducts_deletesAllProductsFromDB() = runBlocking {
        addTwoProductsToDb()
        productDao.delete(product1)
        productDao.delete(product2)
        val allProducts = productDao.getAllProducts().first()
        assertTrue(allProducts.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateProducts_updatesProductsInDB() = runBlocking {
        addTwoProductsToDb()
        productDao.update(Product(1, "Apples", 15.0, "fruit"))
        productDao.update(Product(2, "Bananas", 5.0, "fruit"))

        val allProducts = productDao.getAllProducts().first()
        assertEquals(allProducts[0], Product(1, "Apples", 15.0, "fruit"))
        assertEquals(allProducts[1], Product(2, "Bananas", 5.0, "fruit"))
    }

    private suspend fun addOneProductToDb() {
        productDao.insert(product1)
    }

    private suspend fun addTwoProductsToDb() {
        productDao.insert(product1)
        productDao.insert(product2)
    }
}
