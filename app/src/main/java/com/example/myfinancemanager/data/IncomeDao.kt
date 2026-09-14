package com.example.myfinancemanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Query("SELECT * FROM income ORDER BY date DESC")
    fun getAll(): Flow<List<IncomeEntity>>

    @Query("SELECT * FROM income WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    fun getBetween(from: Long, to: Long): Flow<List<IncomeEntity>>

    @Query("SELECT * FROM income WHERE id = :id")
    suspend fun getById(id: Long): IncomeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: IncomeEntity): Long

    @Update
    suspend fun update(entity: IncomeEntity)

    @Delete
    suspend fun delete(entity: IncomeEntity)

    @Query("SELECT * FROM income ORDER BY date DESC")
    suspend fun getAllOnce(): List<IncomeEntity>

    @Query("SELECT * FROM income WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    suspend fun getAllOnceBetween(from: Long, to: Long): List<IncomeEntity>
}