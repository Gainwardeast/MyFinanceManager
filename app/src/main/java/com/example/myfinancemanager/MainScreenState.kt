package com.example.myfinancemanager

import com.example.myfinancemanager.model.FinanceData
import com.example.myfinancemanager.model.PieChartEntry
import java.time.YearMonth
import java.util.Calendar

data class MainScreenState(
    val financeData: FinanceData = FinanceData(),
    val pieChartData: List<PieChartEntry> = emptyList(),
    val balance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val selectedMonthIndex: Int = Calendar.getInstance().get(Calendar.MONTH),
)