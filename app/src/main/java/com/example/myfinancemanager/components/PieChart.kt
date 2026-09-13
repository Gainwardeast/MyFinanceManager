package com.example.myfinancemanager.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
import com.example.myfinancemanager.model.PieChartEntry

@Composable
fun PieChart(
    data: List<PieChartEntry>,
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

    val total = data.sumOf { it.value }
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
                val canvasSize = size.minDimension
                val strokeWidth = canvasSize * 0.75f
                val radius = (canvasSize - strokeWidth) / 2f

                val center = Offset(size.width / 2f, size.height / 2f)

                val arcSize = Size(radius * 2f, radius * 2f)
                val topLeft = Offset(center.x - radius, center.y - radius)

                var startAngle = -90f

                data.forEachIndexed { index, entry ->
                    val sweepAngle = (entry.value / total * 360f).toFloat()

                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth)
                    )
                    startAngle += sweepAngle
                }
            }

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
            data.forEachIndexed { index, entry ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            drawCircle(color = colors[index % colors.size])
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = entry.label,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "₽${String.format("%.0f", entry.value)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}