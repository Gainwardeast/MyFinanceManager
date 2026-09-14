package com.example.myfinancemanager.repository

import com.example.myfinancemanager.data.ExpenseType
import com.example.myfinancemanager.model.Expense
import com.example.myfinancemanager.model.Income
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface FinanceRepository {

    // -------- Доходы --------
    fun getAllIncomes(): Flow<List<Income>>
    fun getIncomesBetween(from: Long, to: Long): Flow<List<Income>>
    suspend fun addIncome(source: String, amount: BigDecimal, date: Long)
    suspend fun removeIncome(id: Long)

    // -------- Расходы --------
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesByType(type: ExpenseType): Flow<List<Expense>>
    fun getExpensesBetween(from: Long, to: Long): Flow<List<Expense>>
    suspend fun addExpense(name: String, amount: BigDecimal, date: Long, type: ExpenseType)
    suspend fun removeExpense(id: Long)
    suspend fun toggleExpensePaid(id: Long)     // если isPaid появится, пока можно убрать
}