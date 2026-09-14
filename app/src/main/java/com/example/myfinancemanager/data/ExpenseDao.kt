package com.example.myfinancemanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense ORDER BY date DESC")
    fun getAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE type = :type ORDER BY date DESC")
    fun getByType(type: ExpenseType): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    fun getBetween(from: Long, to: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE type = :type AND date BETWEEN :from AND :to ORDER BY date DESC")
    fun getByTypeBetween(type: ExpenseType, from: Long, to: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE id = :id")
    suspend fun getById(id: Long): ExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExpenseEntity): Long

    @Update
    suspend fun update(entity: ExpenseEntity)

    @Delete
    suspend fun delete(entity: ExpenseEntity)

    @Query("SELECT * FROM expense ORDER BY date DESC")
    suspend fun getAllOnce(): List<ExpenseEntity>

    @Query("SELECT * FROM expense WHERE type = :type ORDER BY date DESC")
    suspend fun getAllOnceByType(type: ExpenseType): List<ExpenseEntity>

    @Query("SELECT * FROM expense WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    suspend fun getAllOnceBetween(from: Long, to: Long): List<ExpenseEntity>
}