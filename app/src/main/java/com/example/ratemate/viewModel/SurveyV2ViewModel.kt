package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.SurveyV2
import com.example.ratemate.repository.SurveyV2Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SurveyV2ViewModelFactory(private val repository: SurveyV2Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurveyV2ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SurveyV2ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SurveyV2ViewModel(private val repository: SurveyV2Repository) : ViewModel() {

    private val _surveys = MutableStateFlow<List<SurveyV2>>(emptyList())
    val surveys: StateFlow<List<SurveyV2>> = _surveys.asStateFlow()

    private val _survey = MutableStateFlow<SurveyV2?>(null)
    val survey: StateFlow<SurveyV2?> = _survey.asStateFlow()

    fun addSurvey(survey: SurveyV2) {
        repository.addSurvey(survey)
    }

    fun deleteSurvey(surveyId: String) {
        repository.deleteSurvey(surveyId)
    }

    fun updateSurvey(surveyId: String, updatedFields: Map<String, Any>) {
        repository.updateSurvey(surveyId, updatedFields)
    }

    fun getAllSurveys() {
        viewModelScope.launch {
            repository.getAllSurveys().collect {
                _surveys.value = it
            }
        }
    }

    fun getSurvey(surveyId: String) {
        viewModelScope.launch {
            repository.getSurvey(surveyId).collect {
                _survey.value = it
            }
        }
    }

    fun sortSurveys(sortType: SortType) {
        viewModelScope.launch {
            val sortedSurveys = when (sortType) {
                SortType.LATEST -> _surveys.value.sortedByDescending { it.createdDate }
                SortType.MOST_LIKED -> _surveys.value.sortedByDescending { it.likes.count }
                SortType.MOST_RESPONDED -> _surveys.value.sortedByDescending { it.response.size }
            }
            _surveys.value = sortedSurveys
        }
    }
}

enum class SortType {
    LATEST, MOST_LIKED, MOST_RESPONDED
}
