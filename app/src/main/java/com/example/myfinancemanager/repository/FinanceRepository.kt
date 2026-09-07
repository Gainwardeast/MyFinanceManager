package com.example.myfinancemanager.repository


import com.example.myfinancemanager.model.CurrentExpense
import com.example.myfinancemanager.model.ExpenseCategory
import com.example.myfinancemanager.model.FinanceData
import com.example.myfinancemanager.model.FixedExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface FinanceRepository {
    fun getFinanceData(): Flow<FinanceData>
    fun getCategoriesForPieChartFlow(): Flow<List<ExpenseCategory>> // <-- Новый метод
    suspend fun updateMonthlyIncome(amount: Double)
    suspend fun addFixedExpense(expense: FixedExpense)
    suspend fun removeFixedExpense(id: Int)
    suspend fun toggleFixedExpensePaid(id: Int)
    suspend fun addCurrentExpense(expense: CurrentExpense)
    suspend fun removeCurrentExpense(id: Int)
    suspend fun getCategoriesForPieChart(): List<ExpenseCategory> // Оставляем для других нужд
}

class MockFinanceRepository : FinanceRepository {

    private var monthlyIncome = MutableStateFlow(85000.0)

    private var fixedExpenses = MutableStateFlow(
        listOf(
            FixedExpense(1, "Аренда квартиры", 25000.0, false),
            FixedExpense(2, "Коммунальные услуги", 5000.0, false),
            FixedExpense(3, "Интернет", 800.0, false),
            FixedExpense(4, "Кредит", 12000.0, false)
        )
    )

    private var currentExpenses = MutableStateFlow(
        listOf(
            CurrentExpense(1, "Продукты", 4500.0, "Сегодня"),
            CurrentExpense(2, "Кафе", 1200.0, "Вчера"),
            CurrentExpense(3, "Бензин", 2000.0, "2 дня назад"),
            CurrentExpense(4, "Развлечения", 1500.0, "3 дня назад")
        )
    )

    private var nextFixedId = 5
    private var nextCurrentId = 5

    override fun getFinanceData(): Flow<FinanceData> {
        return MutableStateFlow(
            FinanceData(
                monthlyIncome = monthlyIncome.value,
                fixedExpenses = fixedExpenses.value,
                currentExpenses = currentExpenses.value
            )
        )
    }

    // НОВЫЙ МЕТОД: возвращает Flow для диаграммы
    override suspend fun getCategoriesForPieChartFlow(): Flow<List<ExpenseCategory>> {
        return MutableStateFlow(getCategoriesForPieChart())
    }

    override suspend fun updateMonthlyIncome(amount: Double) {
        monthlyIncome.update { amount }
    }

    override suspend fun addFixedExpense(expense: FixedExpense) {
        fixedExpenses.update { current ->
            current + expense.copy(id = nextFixedId++)
        }
    }

    override suspend fun removeFixedExpense(id: Int) {
        fixedExpenses.update { current ->
            current.filter { it.id != id }
        }
    }

    override suspend fun toggleFixedExpensePaid(id: Int) {
        fixedExpenses.update { current ->
            current.map {
                if (it.id == id) it.copy(isPaid = !it.isPaid) else it
            }
        }
    }

    override suspend fun addCurrentExpense(expense: CurrentExpense) {
        currentExpenses.update { current ->
            current + expense.copy(id = nextCurrentId++)
        }
    }

    override suspend fun removeCurrentExpense(id: Int) {
        currentExpenses.update { current ->
            current.filter { it.id != id }
        }
    }

    override suspend fun getCategoriesForPieChart(): List<ExpenseCategory> {
        val allExpenses = fixedExpenses.value.map {
            ExpenseCategory(it.name, it.amount, getColorForCategory(it.name))
        } + currentExpenses.value.map {
            ExpenseCategory(it.name, it.amount, getColorForCategory(it.name))
        }
        return allExpenses
    }

    private fun getColorForCategory(name: String): Long {
        return when (name) {
            "Аренда квартиры" -> 0xFFE57373.toLong()
            "Коммунальные услуги" -> 0xFF81C784.toLong()
            "Интернет" -> 0xFF64B5F6.toLong()
            "Кредит" -> 0xFFFFD54F.toLong()
            "Продукты" -> 0xFFFF8A65.toLong()
            "Кафе" -> 0xFFCE93D8.toLong()
            "Бензин" -> 0xFF4DD0E1.toLong()
            "Развлечения" -> 0xFFFFB74D.toLong()
            else -> 0xFFB0BEC5.toLong()
        }
    }
}