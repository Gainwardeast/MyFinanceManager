package com.example.myfinancemanager.data

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converters {

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }

    @TypeConverter
    fun fromExpenseType(type: ExpenseType): String = type.name

    @TypeConverter
    fun toExpenseType(value: String): ExpenseType = ExpenseType.valueOf(value)
}