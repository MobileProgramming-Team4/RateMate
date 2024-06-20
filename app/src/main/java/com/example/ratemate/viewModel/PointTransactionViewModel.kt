package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.PointTransaction
import com.example.ratemate.repository.PointTransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class PointTransactionViewModelFactory(private val repository: PointTransactionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PointTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PointTransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class PointTransactionViewModel(private val repository: PointTransactionRepository) : ViewModel() {

    // 모든 포인트 거래 목록을 위한 StateFlow
    private val _pointTransactions = MutableStateFlow<List<PointTransaction>>(emptyList())
    val pointTransactions: StateFlow<List<PointTransaction>> = _pointTransactions.asStateFlow()

    // 특정 포인트 거래를 위한 StateFlow
    private val _pointTransaction = MutableStateFlow<PointTransaction?>(null)
    val pointTransaction: StateFlow<PointTransaction?> = _pointTransaction.asStateFlow()

    init {
        // 모든 포인트 거래 로드
        loadAllPointTransactions()
    }

    fun loadAllPointTransactions() {
        viewModelScope.launch {
            repository.getAllPointTransactions().collect { transactions ->
                _pointTransactions.value = transactions
            }
        }
    }

    fun getPointTransaction(transactionId: String) {
        viewModelScope.launch {
            repository.getPointTransaction(transactionId).collect { transaction ->
                _pointTransaction.value = transaction
            }
        }
    }

    fun addPointTransaction(transaction: PointTransaction) {
        repository.addPointTransaction(transaction)
    }

    fun deletePointTransaction(transactionId: String) {
        repository.deletePointTransaction(transactionId)
    }

    fun updatePointTransaction(transactionId: String, updatedFields: Map<String, Any>) {
        repository.updatePointTransaction(transactionId, updatedFields)
    }
}
