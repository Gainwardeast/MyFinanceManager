package com.example.myfinancemanager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfinancemanager.components.CurrentExpensesSection
import com.example.myfinancemanager.components.FixedExpensesSection
import com.example.myfinancemanager.components.IncomeSection
import com.example.myfinancemanager.components.PieChart
import com.example.myfinancemanager.repository.MockFinanceRepository

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(MockFinanceRepository())
    )
) {
    val financeData by viewModel.financeData.collectAsState()
    val pieChartData by viewModel.pieChartData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("📊 Планировщик финансов") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Информация о балансе
                val totalFixed = financeData.fixedExpenses.sumOf { it.amount }
                val totalCurrent = financeData.currentExpenses.sumOf { it.amount }
                val totalExpenses = totalFixed + totalCurrent
                val balance = financeData.monthlyIncome - totalExpenses

                // Круговая диаграмма
                PieChart(
                    data = pieChartData,
                    modifier = Modifier.fillMaxWidth()
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (balance >= 0)
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
                                text = "₽${String.format("%,.0f", balance)}",
                                fontSize = 24.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Всего расходов",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₽${String.format("%,.0f", totalExpenses)}",
                                fontSize = 20.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        }
                    }
                }

                // Обязательные расходы
                FixedExpensesSection(
                    expenses = financeData.fixedExpenses,
                    onAddExpense = { name, amount -> viewModel.addFixedExpense(name, amount) },
                    onRemoveExpense = { id -> viewModel.removeFixedExpense(id) },
                    onTogglePaid = { id -> viewModel.toggleFixedExpensePaid(id) },
                    modifier = Modifier.fillMaxWidth()
                )

                // Текущие расходы
                CurrentExpensesSection(
                    expenses = financeData.currentExpenses,
                    onAddExpense = { name, amount -> viewModel.addCurrentExpense(name, amount) },
                    onRemoveExpense = { id -> viewModel.removeCurrentExpense(id) },
                    modifier = Modifier.fillMaxWidth()
                )

                // Доходы
                IncomeSection(
                    currentIncome = financeData.monthlyIncome,
                    onIncomeUpdate = { viewModel.updateMonthlyIncome(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Ошибка
        error?.let {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("ОК")
                    }
                }
            ) {
                Text(it)
            }
        }
    }
}