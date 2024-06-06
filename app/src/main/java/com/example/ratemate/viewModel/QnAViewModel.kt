package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.QnA
import com.example.ratemate.repository.QnARepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QnAViewModelFactory(private val qnaRepository: QnARepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QnAViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QnAViewModel(qnaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class QnAViewModel (private val qnaRepository: QnARepository) : ViewModel() {

    private val _qnas = MutableStateFlow<List<QnA>>(emptyList())
    val qnas :StateFlow<List<QnA>> = _qnas.asStateFlow()

    private val _qna = MutableStateFlow<QnA?>(null)
    val qna : StateFlow<QnA?> = _qna.asStateFlow()

    init {
        getAllQnAs()
    }


    fun addQnA(qnA : QnA) {
        qnaRepository.addQnA(qnA)
    }

    fun deleteQnA(order: Int) {
        qnaRepository.deleteQnA(order)
    }

    fun updateQnA(order: Int, updatedFields: Map<String, Any>) {
        qnaRepository.updateQnA(order, updatedFields)
    }

    fun getAllQnAs() {
        viewModelScope.launch {
            qnaRepository.getAllQnAs().collect{
                _qnas.value = it
            }
        }
    }

    fun getQnA(order: Int) {
        viewModelScope.launch {
            qnaRepository.getQnA(order).collect{
                _qna.value = it
            }
        }
    }
}