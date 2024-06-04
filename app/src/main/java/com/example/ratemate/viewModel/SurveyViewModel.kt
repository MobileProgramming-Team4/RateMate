package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.Survey
import com.example.ratemate.repository.SurveyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SurveyModelFactory(private val repository: SurveyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurveyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SurveyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SurveyViewModel(private val repository: SurveyRepository) : ViewModel() {
    private val _surveys = MutableStateFlow<List<Survey>?>(null)
    val surveys: StateFlow<List<Survey>?> = _surveys.asStateFlow()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    init {
        loadAllSurveys()
    }

    private fun loadAllSurveys() {
        viewModelScope.launch {
            repository.getAllSurveys().collect { surveys ->
                _surveys.value = surveys
            }
        }
    }

    fun sortSurveys(sortType: SortType) {
        viewModelScope.launch {
            val sortedSurveys = when (sortType) {
                SortType.LATEST -> _surveys.value?.sortedByDescending { parseDate(it.createdDate) }
                SortType.MOST_LIKED -> _surveys.value?.sortedByDescending { it.likes }
                SortType.MOST_RESPONDED -> _surveys.value?.sortedByDescending { it.responses }
            }
            _surveys.value = sortedSurveys
        }
    }

    private fun parseDate(dateString: String): Date {
        return try {
            dateFormat.parse(dateString) ?: Date(0)
        } catch (e: Exception) {
            Date(0)
        }
    }
}

enum class SortType {
    LATEST, MOST_LIKED, MOST_RESPONDED
}
