package com.example.myfinancemanager.model

data class ExpenseCategory(
    val name: String,
    val amount: Double,
    val color: Long
)

data class FinanceData(
    val monthlyIncome: Double = 0.0,
    val fixedExpenses: List<FixedExpense> = emptyList(),
    val currentExpenses: List<CurrentExpense> = emptyList()
)

data class FixedExpense(
    val id: Int,
    val name: String,
    val amount: Double,
    val isPaid: Boolean = false
)

data class CurrentExpense(
    val id: Int,
    val name: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis()
)