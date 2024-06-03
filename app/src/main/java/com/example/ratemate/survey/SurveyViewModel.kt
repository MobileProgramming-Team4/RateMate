package com.example.ratemate.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.Survey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.ratemate.repository.SurveyRepository


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
    private val _surveys = MutableLiveData<List<Survey>?>()
    val surveys: LiveData<List<Survey>?> = _surveys

    init {
        loadSurveys()
    }

    private fun loadSurveys() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllSurveys().collect { surveyList ->
                _surveys.postValue(surveyList)
            }
        }
    }

    fun sortSurveys(sortType: SortType) {
        val sortedList = _surveys.value?.let {
            when (sortType) {
                SortType.LATEST -> it.sortedBy { survey -> survey.createdDate }
                SortType.MOST_LIKED -> it.sortedByDescending { survey -> survey.likes }
                SortType.MOST_RESPONDED -> it.sortedByDescending { survey -> survey.responses }
            }
        }
        _surveys.postValue(sortedList)
    }
}

enum class SortType {
    LATEST, MOST_LIKED, MOST_RESPONDED
}