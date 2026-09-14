package com.example.myfinancemanager


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfinancemanager.data.ExpenseType
import com.example.myfinancemanager.model.FinanceData
import com.example.myfinancemanager.model.PieChartEntry
import com.example.myfinancemanager.repository.FinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Calendar

class MainViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        observeFinanceData()
    }

    private fun observeFinanceData() {
        combine(
            repository.getAllIncomes(),
            repository.getAllExpenses()
        ) { incomes, expenses ->
            FinanceData(incomes = incomes, expenses = expenses)
        }
            .onEach { data ->
                _state.value = _state.value.copy(financeData = data)
                recalculate()
            }
            .launchIn(viewModelScope)
    }

    // ======================== ВЫБОР МЕСЯЦА ========================

    fun previousMonth() = shiftMonth(-1)

    fun nextMonth() = shiftMonth(1)

    fun selectMonth(year: Int, monthIndex: Int) {
        _state.value = _state.value.copy(
            selectedYear = year,
            selectedMonthIndex = monthIndex
        )
        recalculate()
    }

    private fun shiftMonth(delta: Int) {
        val state = _state.value
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, state.selectedYear)
            set(Calendar.MONTH, state.selectedMonthIndex)
        }
        cal.add(Calendar.MONTH, delta)
        _state.value = state.copy(
            selectedYear = cal.get(Calendar.YEAR),
            selectedMonthIndex = cal.get(Calendar.MONTH)
        )
        recalculate()
    }

    // ======================== ДОХОДЫ ========================

    fun addIncome(source: String, amount: BigDecimal) {
        viewModelScope.launch {
            runCatching {
                repository.addIncome(source, amount, System.currentTimeMillis())
            }.onFailure { _state.value = _state.value.copy(error = it.message) }
        }
    }

    fun removeIncome(id: Long) {
        viewModelScope.launch {
            runCatching { repository.removeIncome(id) }
                .onFailure { _state.value = _state.value.copy(error = it.message) }
        }
    }

    // ======================== РАСХОДЫ ========================

    fun addFixedExpense(name: String, amount: Double) {
        viewModelScope.launch {
            runCatching {
                repository.addExpense(
                    name = name,
                    amount = amount.toBigDecimal(),
                    date = System.currentTimeMillis(),
                    type = ExpenseType.MANDATORY
                )
            }.onFailure { _state.value = _state.value.copy(error = it.message) }
        }
    }

    fun addCurrentExpense(name: String, amount: Double) {
        viewModelScope.launch {
            runCatching {
                repository.addExpense(
                    name = name,
                    amount = amount.toBigDecimal(),
                    date = System.currentTimeMillis(),
                    type = ExpenseType.OPTIONAL
                )
            }.onFailure { _state.value = _state.value.copy(error = it.message) }
        }
    }

    fun removeExpense(id: Long) {
        viewModelScope.launch {
            runCatching { repository.removeExpense(id) }
                .onFailure { _state.value = _state.value.copy(error = it.message) }
        }
    }

    // ======================== ПЕРЕСЧЁТ ========================

    private fun recalculate() {
        val state = _state.value
        val data = state.financeData

        val from = monthStartMillis(state.selectedYear, state.selectedMonthIndex)
        val to = monthEndMillis(state.selectedYear, state.selectedMonthIndex)

        val monthlyIncomes = data.incomes.filter { it.date in from..to }
        val monthlyExpenses = data.expenses.filter { it.date in from..to }

        val totalIncome = monthlyIncomes.fold(BigDecimal.ZERO) { acc, i -> acc + i.amount }
        val totalExpense = monthlyExpenses.fold(BigDecimal.ZERO) { acc, e -> acc + e.amount }
        val balance = totalIncome - totalExpense

        val totalMandatory = monthlyExpenses
            .filter { it.type == ExpenseType.MANDATORY }
            .fold(BigDecimal.ZERO) { acc, e -> acc + e.amount }

        val totalOptional = monthlyExpenses
            .filter { it.type == ExpenseType.OPTIONAL }
            .fold(BigDecimal.ZERO) { acc, e -> acc + e.amount }

        val pieData = buildList {
            if (totalMandatory > BigDecimal.ZERO) add(
                PieChartEntry("Обязательные", totalMandatory.toDouble(), 0xFFEF5350)
            )
            if (totalOptional > BigDecimal.ZERO) add(
                PieChartEntry("Текущие", totalOptional.toDouble(), 0xFF42A5F5)
            )
        }

        _state.value = state.copy(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance,
            pieChartData = pieData,
            monthlyIncomes = monthlyIncomes.sortedByDescending { it.date },
            monthlyExpenses = monthlyExpenses.sortedByDescending { it.date }
        )
    }
    private fun monthStartMillis(year: Int, monthIndex: Int): Long =
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthIndex)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    private fun monthEndMillis(year: Int, monthIndex: Int): Long =
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthIndex)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
            add(Calendar.MONTH, 1)
            add(Calendar.MILLISECOND, -1)
        }.timeInMillis

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun toggleFixedExpensePaid(id: Long) {
        viewModelScope.launch {
            runCatching { repository.toggleExpensePaid(id) }
                .onFailure { _state.value = _state.value.copy(error = it.message) }
        }
    }
}