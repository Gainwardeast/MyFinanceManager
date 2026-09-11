package com.example.myfinancemanager.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfinancemanager.model.ExpenseCategory

@Composable
fun PieChart(
    data: List<ExpenseCategory>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Нет данных для отображения",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val total = data.sumOf { it.amount }
    val colors = data.map { Color(it.color) }
    val totalAmount = String.format("%.0f", total)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // === ДИАГРАММА ===
        Box(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Размер холста
                val canvasSize = size.minDimension
                val strokeWidth = canvasSize * 0.75f  // толщина обводки = 25% диаметра
                val radius = (canvasSize - strokeWidth) / 2f  // радиус центра окружности

                // Центр холста
                val center = Offset(
                    x = size.width / 2f,
                    y = size.height / 2f
                )

                // Top-left квадрата, в который вписывается окружность
                val arcSize = Size(radius * 2f, radius * 2f)
                val topLeft = Offset(
                    x = center.x - radius,
                    y = center.y - radius
                )

                var startAngle = -90f  // начинаем сверху

                data.forEachIndexed { index, category ->
                    val sweepAngle = (category.amount / total * 360f).toFloat()

                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth)
                        // Без cap = Round — края сегментов будут ровные
                    )
                    startAngle += sweepAngle
                }
            }

            // Центральный текст
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "₽$totalAmount",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Всего расходов",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === ЛЕГЕНДА ===
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            data.forEachIndexed { index, category ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Цветной кружок
                        Canvas(modifier = Modifier.size(12.dp)) {
                            drawCircle(color = colors[index % colors.size])
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = category.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "₽${String.format("%.0f", category.amount)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}