package com.example.myfinancemanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(income: IncomeEntity): Long

    @Update
    suspend fun update(income: IncomeEntity)

    @Delete
    suspend fun delete(income: IncomeEntity)

    @Query("SELECT * FROM income ORDER BY date DESC")
    fun getAll(): Flow<List<IncomeEntity>>

    @Query("SELECT * FROM income WHERE id = :id")
    suspend fun getById(id: Long): IncomeEntity?

    @Query("SELECT SUM(CAST(amount AS REAL)) FROM income WHERE date BETWEEN :from AND :to")
    suspend fun getTotalBetween(from: Long, to: Long): Double?

    @Query("SELECT * FROM income WHERE date BETWEEN :from AND :to")
    suspend fun getAllOnce(from: Long, to: Long): List<IncomeEntity>
}