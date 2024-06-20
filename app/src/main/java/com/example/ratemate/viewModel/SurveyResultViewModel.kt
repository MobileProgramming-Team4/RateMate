package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.SurveyResult
import com.example.ratemate.repository.SurveyResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SurveyResultViewModelFactory(private val repository: SurveyResultRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurveyResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SurveyResultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class SurveyResultViewModel(private val repository: SurveyResultRepository) : ViewModel() {

    // 모든 설문 결과 목록을 위한 StateFlow
    private val _surveyResults = MutableStateFlow<List<SurveyResult>>(emptyList())
    val surveyResults: StateFlow<List<SurveyResult>> = _surveyResults.asStateFlow()

    // 특정 설문 결과를 위한 StateFlow
    private val _surveyResult = MutableStateFlow<SurveyResult?>(null)
    val surveyResult: StateFlow<SurveyResult?> = _surveyResult.asStateFlow()

    init {
        // 모든 설문 결과 로드
        loadAllSurveyResults()
    }

    private fun loadAllSurveyResults() {
        viewModelScope.launch {
            repository.getAllSurveyResults().collect { results ->
                _surveyResults.value = results
            }
        }
    }

    fun getSurveyResult(surveyId: String) {
        viewModelScope.launch {
            repository.getSurveyResult(surveyId).collect { result ->
                _surveyResult.value = result
            }
        }
    }

    fun addSurveyResult(surveyResult: SurveyResult) {
        repository.addSurveyResult(surveyResult)
    }

    fun deleteSurveyResult(surveyId: String) {
        repository.deleteSurveyResult(surveyId)
    }

    fun updateSurveyResult(surveyId: String, updatedFields: Map<String, Any>) {
        repository.updateSurveyResult(surveyId, updatedFields)
    }
}
