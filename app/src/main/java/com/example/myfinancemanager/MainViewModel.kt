package com.example.myfinancemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfinancemanager.model.CurrentExpense
import com.example.myfinancemanager.model.FixedExpense
import com.example.myfinancemanager.model.PieChartEntry
import com.example.myfinancemanager.repository.FinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    // ======================== ВЫБОР МЕСЯЦА ========================


    fun previousMonth() {
        val state = _state.value
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, state.selectedYear)
            set(Calendar.MONTH, state.selectedMonthIndex)
        }
        cal.add(Calendar.MONTH, -1)
        _state.value = state.copy(
            selectedYear = cal.get(Calendar.YEAR),
            selectedMonthIndex = cal.get(Calendar.MONTH)
        )
        recalculate()
    }

    fun nextMonth() {
        val state = _state.value
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, state.selectedYear)
            set(Calendar.MONTH, state.selectedMonthIndex)
        }
        cal.add(Calendar.MONTH, 1)
        _state.value = state.copy(
            selectedYear = cal.get(Calendar.YEAR),
            selectedMonthIndex = cal.get(Calendar.MONTH)
        )
        recalculate()
    }

    // ======================== ДОХОДЫ ========================

    fun updateMonthlyIncome(income: Double) {
        viewModelScope.launch {
            try {
                val data = _state.value.financeData.copy(monthlyIncome = income)
                _state.value = _state.value.copy(financeData = data)
                recalculate()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    // ======================== ОБЯЗАТЕЛЬНЫЕ ========================

    fun addFixedExpense(name: String, amount: Double) {
        viewModelScope.launch {
            try {
                val newId = (_state.value.financeData.fixedExpenses.maxOfOrNull { it.id } ?: 0) + 1
                val newItem = FixedExpense(id = newId, name = name, amount = amount)
                val newList = _state.value.financeData.fixedExpenses + newItem
                _state.value = _state.value.copy(
                    financeData = _state.value.financeData.copy(fixedExpenses = newList)
                )
                recalculate()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun removeFixedExpense(id: Int) {
        viewModelScope.launch {
            try {
                val newList = _state.value.financeData.fixedExpenses.filterNot { it.id == id }
                _state.value = _state.value.copy(
                    financeData = _state.value.financeData.copy(fixedExpenses = newList)
                )
                recalculate()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun toggleFixedExpensePaid(id: Int) {
        viewModelScope.launch {
            try {
                val newList = _state.value.financeData.fixedExpenses.map {
                    if (it.id == id) it.copy(isPaid = !it.isPaid) else it
                }
                _state.value = _state.value.copy(
                    financeData = _state.value.financeData.copy(fixedExpenses = newList)
                )
                recalculate()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    // ======================== ТЕКУЩИЕ ========================

    fun addCurrentExpense(name: String, amount: Double) {
        viewModelScope.launch {
            try {
                val newId =
                    (_state.value.financeData.currentExpenses.maxOfOrNull { it.id } ?: 0) + 1
                val newItem = CurrentExpense(
                    id = newId,
                    name = name,
                    amount = amount,
                    date = System.currentTimeMillis()   // ← актуальная дата
                )
                val newList = _state.value.financeData.currentExpenses + newItem
                _state.value = _state.value.copy(
                    financeData = _state.value.financeData.copy(currentExpenses = newList)
                )
                recalculate()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun removeCurrentExpense(id: Int) {
        viewModelScope.launch {
            try {
                val newList = _state.value.financeData.currentExpenses.filterNot { it.id == id }
                _state.value = _state.value.copy(
                    financeData = _state.value.financeData.copy(currentExpenses = newList)
                )
                recalculate()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    // ======================== ПЕРЕСЧЁТ ========================

    private fun recalculate() {
        val state = _state.value
        val data = state.financeData

        // Фильтруем текущие расходы по выбранному месяцу
        val currentForMonth = data.currentExpenses.filter {
            val cal = Calendar.getInstance().apply { timeInMillis = it.date }
            cal.get(Calendar.YEAR) == state.selectedYear &&
                    cal.get(Calendar.MONTH) == state.selectedMonthIndex
        }

        val totalFixed = data.fixedExpenses.sumOf { it.amount }
        val totalCurrent = currentForMonth.sumOf { it.amount }
        val totalExpense = totalFixed + totalCurrent
        val totalIncome = data.monthlyIncome
        val balance = totalIncome - totalExpense

        val pieData = buildList {
            if (totalFixed > 0) add(
                PieChartEntry(
                    label = "Обязательные",
                    value = totalFixed,
                    color = 0xFFEF5350
                )
            )
            if (totalCurrent > 0) add(
                PieChartEntry(
                    label = "Текущие",
                    value = totalCurrent,
                    color = 0xFF42A5F5
                )
            )
        }

        _state.value = state.copy(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance,
            pieChartData = pieData
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun selectMonth(year: Int, monthIndex: Int) {
        _state.value = _state.value.copy(
            selectedYear = year,
            selectedMonthIndex = monthIndex
        )
        recalculate()
    }
}