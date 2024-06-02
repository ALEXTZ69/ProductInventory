package com.example.cart.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * from carts ORDER BY name ASC")
    fun getAllCarts(): Flow<List<Cart>>

    @Query("SELECT * from carts WHERE id = :id")
    fun getDetail(id: Int): Flow<Cart>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(detail: Cart)

    @Update
    suspend fun update(detail: Cart)

    @Delete
    suspend fun delete(detail: Cart)
}
