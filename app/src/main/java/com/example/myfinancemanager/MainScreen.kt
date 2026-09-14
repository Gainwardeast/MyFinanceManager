package com.example.myfinancemanager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfinancemanager.components.CurrentExpensesSection
import com.example.myfinancemanager.components.FixedExpensesSection
import com.example.myfinancemanager.components.IncomeSection
import com.example.myfinancemanager.components.MonthSelector
import com.example.myfinancemanager.components.MonthYearPickerDialog
import com.example.myfinancemanager.components.PieChart
import com.example.myfinancemanager.data.ExpenseType
import com.example.myfinancemanager.repository.FinanceRepository
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    repository: FinanceRepository,
    viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(repository)
    ),
) {
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    var showMonthPicker by remember { mutableStateOf(false) }

    if (showMonthPicker) {
        MonthYearPickerDialog(
            initialYear = state.selectedYear,
            initialMonthIndex = state.selectedMonthIndex,
            onDismiss = { showMonthPicker = false },
            onConfirm = { year, monthIndex ->
                viewModel.selectMonth(year, monthIndex)
                showMonthPicker = false
            }
        )
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📊 Планировщик финансов") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val financeData = state.financeData

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 0. Выбор месяца
                MonthSelector(
                    year = state.selectedYear,
                    monthIndex = state.selectedMonthIndex,
                    onPrevious = { viewModel.previousMonth() },
                    onNext = { viewModel.nextMonth() },
                    onSelectClick = { showMonthPicker = true }
                )

                // 1. Диаграмма
                PieChart(
                    data = state.pieChartData,
                    modifier = Modifier.fillMaxWidth()
                )

                // 2. Баланс
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.balance >= BigDecimal.ZERO)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Баланс",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₽${String.format("%,.0f", state.balance)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Всего расходов",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₽${String.format("%,.0f", state.totalExpense)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // 3. Обязательные расходы
                FixedExpensesSection(
                    expenses = financeData.expenses.filter { it.type == ExpenseType.MANDATORY },
                    onAddExpense = { name, amount -> viewModel.addFixedExpense(name, amount) },
                    onRemoveExpense = { id -> viewModel.removeExpense(id) },
                    onTogglePaid = { id -> viewModel.toggleFixedExpensePaid(id) },   // ← вернули
                    modifier = Modifier.fillMaxWidth()
                )

                // 4. Текущие расходы
                CurrentExpensesSection(
                    expenses = financeData.expenses.filter { it.type == ExpenseType.OPTIONAL },
                    onAddExpense = { name, amount -> viewModel.addCurrentExpense(name, amount) },
                    onRemoveExpense = { id -> viewModel.removeExpense(id) },
                    modifier = Modifier.fillMaxWidth()
                )

                // 5. Доходы
                IncomeSection(
                    incomes = state.monthlyIncomes,
                    totalIncome = state.totalIncome,
                    onAddIncome = { source, amount -> viewModel.addIncome(source, amount) },
                    onRemoveIncome = { id -> viewModel.removeIncome(id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}