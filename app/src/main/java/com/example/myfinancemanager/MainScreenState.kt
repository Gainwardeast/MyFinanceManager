package com.example.myfinancemanager

import com.example.myfinancemanager.model.Expense
import com.example.myfinancemanager.model.FinanceData
import com.example.myfinancemanager.model.Income
import com.example.myfinancemanager.model.PieChartEntry
import java.math.BigDecimal
import java.time.YearMonth
import java.util.Calendar

data class MainScreenState(
    val financeData: FinanceData = FinanceData(),
    val pieChartData: List<PieChartEntry> = emptyList(),
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val selectedMonthIndex: Int = Calendar.getInstance().get(Calendar.MONTH),
    val balance: BigDecimal = BigDecimal.ZERO,
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,
    val monthlyIncomes: List<Income> = emptyList(),        // ← добавили
    val monthlyExpenses: List<Expense> = emptyList(),      // ← и это тоже пригодится
    val isLoading: Boolean = false,
    val error: String? = null
)