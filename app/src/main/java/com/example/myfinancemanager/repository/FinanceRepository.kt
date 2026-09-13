package com.example.myfinancemanager.repository

import com.example.myfinancemanager.data.ExpenseEntity
import com.example.myfinancemanager.data.ExpenseType
import com.example.myfinancemanager.data.IncomeEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface FinanceRepository {

    // ======================== ДОХОДЫ ========================
    fun getAllIncomes(): Flow<List<IncomeEntity>>
    suspend fun getIncomeById(id: Long): IncomeEntity?
    suspend fun insertIncome(income: IncomeEntity): Long
    suspend fun updateIncome(income: IncomeEntity)
    suspend fun deleteIncome(income: IncomeEntity)

    // ======================== РАСХОДЫ ========================
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
    fun getExpensesByType(type: ExpenseType): Flow<List<ExpenseEntity>>
    suspend fun getExpenseById(id: Long): ExpenseEntity?
    suspend fun insertExpense(expense: ExpenseEntity): Long
    suspend fun updateExpense(expense: ExpenseEntity)
    suspend fun deleteExpense(expense: ExpenseEntity)

    // ======================== АНАЛИТИКА ========================
    val totalIncome: Flow<BigDecimal>
    val totalExpense: Flow<BigDecimal>
    val balance: Flow<BigDecimal>

    suspend fun getIncomeBetween(from: Long, to: Long): BigDecimal
    suspend fun getExpenseBetween(from: Long, to: Long): BigDecimal
}