package com.example.myfinancemanager.repository

import com.example.myfinancemanager.data.ExpenseDao
import com.example.myfinancemanager.data.ExpenseEntity
import com.example.myfinancemanager.data.ExpenseType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

class ExpenseRepository(private val dao: ExpenseDao) {

    fun getAll(): Flow<List<ExpenseEntity>> = dao.getAll()

    fun getByType(type: ExpenseType): Flow<List<ExpenseEntity>> = dao.getByType(type)

    suspend fun getById(id: Long): ExpenseEntity? = dao.getById(id)

    suspend fun insert(expense: ExpenseEntity): Long = dao.insert(expense)

    suspend fun update(expense: ExpenseEntity) = dao.update(expense)

    suspend fun delete(expense: ExpenseEntity) = dao.delete(expense)

    suspend fun getTotalBetween(from: Long, to: Long): BigDecimal {
        val items = dao.getAllOnce(from, to)
        return items.fold(BigDecimal.ZERO) { acc, item -> acc + item.amount }
    }
}