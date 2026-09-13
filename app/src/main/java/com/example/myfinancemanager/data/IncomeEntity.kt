package com.example.myfinancemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "income")
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val source: String,              // источник дохода
    val amount: BigDecimal,          // сумма
    val date: Long,                  // дата (timestamp)
    val comment: String? = null,     // комментарий
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)