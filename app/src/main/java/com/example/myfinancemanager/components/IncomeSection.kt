package com.example.myfinancemanager.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IncomeSection(
    currentIncome: Double,
    onIncomeUpdate: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var incomeText by remember { mutableStateOf(if (currentIncome > 0) currentIncome.toString() else "") }
    var isEditing by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
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
                    text = "💰 Средний доход в месяц",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                if (!isEditing) {
                    TextButton(onClick = { isEditing = true }) {
                        Text("Изменить")
                    }
                }
            }

            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = incomeText,
                        onValueChange = { incomeText = it },
                        label = { Text("Введите доход") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small
                    )

                    TextButton(
                        onClick = {
                            val amount = incomeText.toDoubleOrNull()
                            if (amount != null && amount > 0) {
                                onIncomeUpdate(amount)
                                isEditing = false
                            }
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            } else {
                Text(
                    text = "₽${String.format("%,.0f", currentIncome)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}