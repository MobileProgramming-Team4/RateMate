package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.Question
import com.example.ratemate.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionViewModelFactory(private val repository: QuestionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuestionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class QuestionViewModel(private val repository: QuestionRepository) : ViewModel() {

    // 모든 질문 목록을 위한 StateFlow
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    // 특정 질문을 위한 StateFlow
    private val _question = MutableStateFlow<List<Question>>(emptyList())
    val question: StateFlow<List<Question>> = _question.asStateFlow()

    init {
        // 모든 질문 로드
        loadAllQuestions()
    }

    private fun loadAllQuestions() {
        viewModelScope.launch {
            repository.getAllQuestions().collect { questions ->
                _questions.value = questions
            }
        }
    }

    fun getQuestionsByContent(content: String) {
        viewModelScope.launch {
            repository.getQuestionsByContent(content).collect { questions ->
                _question.value = questions
            }
        }
    }

    fun insertQuestion(question: Question) {
        repository.insertQuestion(question)
    }

    fun deleteQuestion(questionId: String) {
        repository.deleteQuestion(questionId)
    }

    fun updateQuestion(questionId: String, updatedFields: Map<String, Any>) {
        repository.updateQuestion(questionId, updatedFields)
    }
}
