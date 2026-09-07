package com.example.myfinancemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfinancemanager.model.CurrentExpense
import com.example.myfinancemanager.model.ExpenseCategory
import com.example.myfinancemanager.model.FinanceData
import com.example.myfinancemanager.model.FixedExpense
import com.example.myfinancemanager.repository.FinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _financeData = MutableStateFlow(
        FinanceData(
            monthlyIncome = 0.0,
            fixedExpenses = emptyList(),
            currentExpenses = emptyList()
        )
    )
    val financeData: StateFlow<FinanceData> = _financeData.asStateFlow()

    private val _pieChartData = MutableStateFlow<List<ExpenseCategory>>(emptyList())
    val pieChartData: StateFlow<List<ExpenseCategory>> = _pieChartData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                combine(
                    repository.getFinanceData(),
                    repository.getCategoriesForPieChart()
                ) { financeData, categories ->
                    _financeData.value = financeData
                    _pieChartData.value = categories
                }.collect()
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки данных: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    fun updateMonthlyIncome(amount: Double) {
        viewModelScope.launch {
            repository.updateMonthlyIncome(amount)
        }
    }

    fun addFixedExpense(name: String, amount: Double) {
        viewModelScope.launch {
            try {
                val newExpense = FixedExpense(
                    id = 0,
                    name = name,
                    amount = amount,
                    isPaid = false
                )
                repository.addFixedExpense(newExpense)
            } catch (e: Exception) {
                _error.value = "Ошибка добавления: ${e.message}"
            }
        }
    }

    fun removeFixedExpense(id: Int) {
        viewModelScope.launch {
            repository.removeFixedExpense(id)
        }
    }

    fun toggleFixedExpensePaid(id: Int) {
        viewModelScope.launch {
            repository.toggleFixedExpensePaid(id)
        }
    }

    fun addCurrentExpense(name: String, amount: Double) {
        viewModelScope.launch {
            try {
                val newExpense = CurrentExpense(
                    id = 0,
                    name = name,
                    amount = amount,
                    date = "Сегодня"
                )
                repository.addCurrentExpense(newExpense)
            } catch (e: Exception) {
                _error.value = "Ошибка добавления: ${e.message}"
            }
        }
    }

    fun removeCurrentExpense(id: Int) {
        viewModelScope.launch {
            repository.removeCurrentExpense(id)
        }
    }

    fun clearError() {
        _error.value = null
    }
}