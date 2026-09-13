package com.example.myfinancemanager.repository

import com.example.myfinancemanager.data.IncomeDao
import com.example.myfinancemanager.data.IncomeEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

class IncomeRepository(private val dao: IncomeDao) {

    fun getAll(): Flow<List<IncomeEntity>> = dao.getAll()

    suspend fun getById(id: Long): IncomeEntity? = dao.getById(id)

    suspend fun insert(income: IncomeEntity): Long = dao.insert(income)

    suspend fun update(income: IncomeEntity) = dao.update(income)

    suspend fun delete(income: IncomeEntity) = dao.delete(income)

    suspend fun getTotalBetween(from: Long, to: Long): BigDecimal {
        // Суммируем в коде для точности BigDecimal
        val items = dao.getAllOnce(from, to)
        return items.fold(BigDecimal.ZERO) { acc, item -> acc + item.amount }
    }
}