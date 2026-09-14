package com.example.myfinancemanager.repository

import com.example.myfinancemanager.data.AppDatabase
import com.example.myfinancemanager.data.ExpenseEntity
import com.example.myfinancemanager.data.ExpenseType
import com.example.myfinancemanager.data.IncomeEntity
import com.example.myfinancemanager.data.toModel
import com.example.myfinancemanager.model.Expense
import com.example.myfinancemanager.model.Income
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal

class FinanceRepositoryImpl(
    db: AppDatabase
) : FinanceRepository {

    private val expenseDao = db.expenseDao()
    private val incomeDao = db.incomeDao()

    // ======================== ДОХОДЫ ========================

    override fun getAllIncomes(): Flow<List<Income>> =
        incomeDao.getAll().map { it.map { e -> e.toModel() } }

    override fun getIncomesBetween(from: Long, to: Long): Flow<List<Income>> =
        incomeDao.getBetween(from, to).map { it.map { e -> e.toModel() } }

    override suspend fun addIncome(source: String, amount: BigDecimal, date: Long) {
        incomeDao.insert(IncomeEntity(source = source, amount = amount, date = date))
    }

    override suspend fun removeIncome(id: Long) {
        val entity = incomeDao.getById(id) ?: return
        incomeDao.delete(entity)
    }

    // ======================== РАСХОДЫ ========================

    override fun getAllExpenses(): Flow<List<Expense>> =
        expenseDao.getAll().map { it.map { e -> e.toModel() } }

    override fun getExpensesByType(type: ExpenseType): Flow<List<Expense>> =
        expenseDao.getByType(type).map { it.map { e -> e.toModel() } }

    override fun getExpensesBetween(from: Long, to: Long): Flow<List<Expense>> =
        expenseDao.getBetween(from, to).map { it.map { e -> e.toModel() } }

    override suspend fun addExpense(
        name: String,
        amount: BigDecimal,
        date: Long,
        type: ExpenseType
    ) {
        expenseDao.insert(
            ExpenseEntity(name = name, amount = amount, date = date, type = type)
        )
    }

    override suspend fun removeExpense(id: Long) {
        val entity = expenseDao.getById(id) ?: return
        expenseDao.delete(entity)
    }

    override suspend fun toggleExpensePaid(id: Long) {
        val entity = expenseDao.getById(id) ?: return
        expenseDao.update(entity.copy(isPaid = !entity.isPaid))
    }
}