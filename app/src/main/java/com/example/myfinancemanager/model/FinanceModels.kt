package com.example.myfinancemanager.model

import com.example.myfinancemanager.data.ExpenseType
import java.math.BigDecimal

data class ExpenseCategory(
    val name: String,
    val amount: Double,
    val color: Long
)

data class FinanceData(
    val incomes: List<Income> = emptyList(),
    val expenses: List<Expense> = emptyList()
)

data class Expense(
    val id: Long = 0,
    val name: String,
    val amount: BigDecimal,
    val date: Long = System.currentTimeMillis(),
    val type: ExpenseType,
    val comment: String? = null,
    val isPaid: Boolean = false,
)

data class Income(
    val id: Long = 0,
    val source: String,
    val amount: BigDecimal,
    val date: Long = System.currentTimeMillis(),
    val comment: String? = null
)