package com.example.myfinancemanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM expense ORDER BY date DESC")
    fun getAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE type = :type ORDER BY date DESC")
    fun getByType(type: ExpenseType): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE id = :id")
    suspend fun getById(id: Long): ExpenseEntity?

    @Query("SELECT SUM(CAST(amount AS REAL)) FROM expense WHERE date BETWEEN :from AND :to")
    suspend fun getTotalBetween(from: Long, to: Long): Double?

    @Query("SELECT * FROM expense WHERE date BETWEEN :from AND :to")
    suspend fun getAllOnce(from: Long, to: Long): List<ExpenseEntity>
}