package com.example.myfinancemanager.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinancemanager.model.FixedExpense

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixedExpensesSection(
    expenses: List<FixedExpense>,
    onAddExpense: (String, Double) -> Unit,
    onRemoveExpense: (Int) -> Unit,
    onTogglePaid: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏠 Обязательные расходы",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                FilledTonalIconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Добавить расход",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (expenses.isEmpty()) {
                Text(
                    text = "Нет обязательных расходов",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    expenses.forEach { expense ->
                        FixedExpenseItem(
                            expense = expense,
                            onTogglePaid = { onTogglePaid(expense.id) },
                            onRemove = { onRemoveExpense(expense.id) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddExpenseDialog(
            title = "Добавить обязательный расход",
            onDismiss = { showDialog = false },
            onConfirm = { name, amount ->
                onAddExpense(name, amount)
                showDialog = false
            }
        )
    }
}

@Composable
fun FixedExpenseItem(
    expense: FixedExpense,
    onTogglePaid: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = if (expense.isPaid)
            MaterialTheme.colorScheme.secondaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onTogglePaid,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        if (expense.isPaid) Icons.Default.Check else Icons.Default.Check,
                        contentDescription = "Отметить оплаченным",
                        tint = if (expense.isPaid)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column {
                    Text(
                        text = expense.name,
                        fontWeight = if (expense.isPaid) FontWeight.Normal else FontWeight.Medium
                    )
                    if (expense.isPaid) {
                        Text(
                            text = "✅ Оплачено",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₽${String.format("%.0f", expense.amount)}",
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Удалить",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}