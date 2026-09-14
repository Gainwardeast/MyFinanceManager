package com.example.myfinancemanager

import android.app.Application
import com.example.myfinancemanager.data.AppDatabase
import com.example.myfinancemanager.repository.FinanceRepository
import com.example.myfinancemanager.repository.FinanceRepositoryImpl

class MainApplication : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val financeRepository: FinanceRepository by lazy { FinanceRepositoryImpl(database) }

}