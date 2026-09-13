package com.example.myfinancemanager.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

private val monthNames = listOf(
    "Январь", "Февраль", "Март", "Апрель",
    "Май", "Июнь", "Июль", "Август",
    "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
)

@Composable
fun MonthYearPickerDialog(
    initialYear: Int,
    initialMonthIndex: Int,
    onDismiss: () -> Unit,
    onConfirm: (year: Int, monthIndex: Int) -> Unit
) {
    var pickerYear by remember { mutableStateOf(initialYear) }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { pickerYear -= 1 }) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Предыдущий год")
                }
                Text(
                    text = pickerYear.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { pickerYear += 1 },
                    enabled = pickerYear < currentYear + 5
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Следующий год")
                }
            }
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(220.dp)
            ) {
                items(monthNames) { month ->
                    val index = monthNames.indexOf(month)
                    val selected = index == initialMonthIndex && pickerYear == initialYear

                    OutlinedButton(
                        onClick = { onConfirm(pickerYear, index) },
                        colors = if (selected) {
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        },
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = month.take(3),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}