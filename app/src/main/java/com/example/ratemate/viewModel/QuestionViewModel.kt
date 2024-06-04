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
    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question.asStateFlow()

    fun loadQuestionsForSurvey(surveyId: String) {
        viewModelScope.launch {
            repository.getQuestionsForSurvey(surveyId).collect { questions ->
                _questions.value = questions
            }
        }
    }

    fun loadQuestion(surveyId: String, questionId: String) {
        viewModelScope.launch {
            repository.getQuestion(surveyId, questionId).collect { question ->
                _question.value = question
            }
        }
    }

    fun addQuestion(surveyId: String, question: Question) {
        repository.addQuestionToSurvey(surveyId, question)
    }

    fun updateQuestion(surveyId: String, question: Question) {
        repository.updateQuestionInSurvey(surveyId, question)
    }

    fun deleteQuestion(surveyId: String, questionId: String) {
        repository.deleteQuestionFromSurvey(surveyId, questionId)
    }
}
