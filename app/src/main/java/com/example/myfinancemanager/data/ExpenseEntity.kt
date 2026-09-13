package com.example.myfinancemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "expense")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,                // название расхода
    val amount: BigDecimal,          // сумма
    val date: Long,                  // дата (timestamp)
    val type: ExpenseType,           // обязательный / необязательный
    val comment: String? = null,     // комментарий
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)