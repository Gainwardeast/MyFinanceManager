package com.example.myfinancemanager.repository

import com.example.myfinancemanager.data.ExpenseEntity
import com.example.myfinancemanager.data.ExpenseType
import com.example.myfinancemanager.data.IncomeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.math.BigDecimal

class FinanceRepositoryImpl(
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository
) : FinanceRepository {

    // ======================== ДОХОДЫ ========================

    override fun getAllIncomes(): Flow<List<IncomeEntity>> =
        incomeRepository.getAll()

    override suspend fun getIncomeById(id: Long): IncomeEntity? =
        incomeRepository.getById(id)

    override suspend fun insertIncome(income: IncomeEntity): Long =
        incomeRepository.insert(income)

    override suspend fun updateIncome(income: IncomeEntity) =
        incomeRepository.update(income)

    override suspend fun deleteIncome(income: IncomeEntity) =
        incomeRepository.delete(income)

    // ======================== РАСХОДЫ ========================

    override fun getAllExpenses(): Flow<List<ExpenseEntity>> =
        expenseRepository.getAll()

    override fun getExpensesByType(type: ExpenseType): Flow<List<ExpenseEntity>> =
        expenseRepository.getByType(type)

    override suspend fun getExpenseById(id: Long): ExpenseEntity? =
        expenseRepository.getById(id)

    override suspend fun insertExpense(expense: ExpenseEntity): Long =
        expenseRepository.insert(expense)

    override suspend fun updateExpense(expense: ExpenseEntity) =
        expenseRepository.update(expense)

    override suspend fun deleteExpense(expense: ExpenseEntity) =
        expenseRepository.delete(expense)

    // ======================== АНАЛИТИКА ========================

    override val totalIncome: Flow<BigDecimal> = incomeRepository.getAll()
        .map { list -> list.fold(BigDecimal.ZERO) { acc, item -> acc + item.amount } }

    override val totalExpense: Flow<BigDecimal> = expenseRepository.getAll()
        .map { list -> list.fold(BigDecimal.ZERO) { acc, item -> acc + item.amount } }

    override val balance: Flow<BigDecimal> = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }

    override suspend fun getIncomeBetween(from: Long, to: Long): BigDecimal =
        incomeRepository.getTotalBetween(from, to)

    override suspend fun getExpenseBetween(from: Long, to: Long): BigDecimal =
        expenseRepository.getTotalBetween(from, to)
}