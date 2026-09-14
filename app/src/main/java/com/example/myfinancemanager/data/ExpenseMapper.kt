package com.example.myfinancemanager.data
import com.example.myfinancemanager.model.Expense
import com.example.myfinancemanager.model.Income

// ======================== Expense ========================

fun ExpenseEntity.toModel() = Expense(
    id = id,
    name = name,
    amount = amount,
    date = date,
    type = type,
    isPaid = isPaid,
    comment = comment
)

fun Expense.toEntity() = ExpenseEntity(
    id = id,
    name = name,
    amount = amount,
    date = date,
    type = type,
    isPaid = isPaid,
    comment = comment
)

// ======================== Income ========================

fun IncomeEntity.toModel() = Income(
    id = id,
    source = source,
    amount = amount,
    date = date,
    comment = comment
)

fun Income.toEntity() = IncomeEntity(
    id = id,
    source = source,
    amount = amount,
    date = date,
    comment = comment
)