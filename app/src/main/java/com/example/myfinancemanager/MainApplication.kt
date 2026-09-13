package com.example.myfinancemanager

import android.app.Application
import com.example.myfinancemanager.data.AppDatabase
import com.example.myfinancemanager.repository.ExpenseRepository
import com.example.myfinancemanager.repository.FinanceRepository
import com.example.myfinancemanager.repository.FinanceRepositoryImpl
import com.example.myfinancemanager.repository.IncomeRepository

class MainApplication : Application() {

        val database by lazy { AppDatabase.getInstance(this) }
        private val incomeRepository by lazy { IncomeRepository(database.incomeDao()) }
        private val expenseRepository by lazy { ExpenseRepository(database.expenseDao()) }

        // Фасад (через интерфейс)
        val financeRepository: FinanceRepository by lazy {
            FinanceRepositoryImpl(incomeRepository, expenseRepository)
        }
    }