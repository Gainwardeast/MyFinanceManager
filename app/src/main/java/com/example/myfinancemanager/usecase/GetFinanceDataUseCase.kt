package com.example.myfinancemanager.usecase

import com.example.myfinancemanager.model.FinanceData
import com.example.myfinancemanager.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow

class GetFinanceDataUseCase(
    private val repository: FinanceRepository
) {
    operator fun invoke(): Flow<FinanceData> = repository.getFinanceData()
}