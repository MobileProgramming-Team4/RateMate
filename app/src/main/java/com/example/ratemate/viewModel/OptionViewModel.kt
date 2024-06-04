package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.Option
import com.example.ratemate.repository.OptionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OptionViewModelFactory(private val repository: OptionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OptionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OptionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class OptionViewModel(private val repository: OptionRepository) : ViewModel() {

    // 모든 옵션 목록을 위한 StateFlow
    private val _options = MutableStateFlow<List<Option>>(emptyList())
    val options: StateFlow<List<Option>> = _options.asStateFlow()

    // 특정 옵션을 위한 StateFlow
    private val _option = MutableStateFlow<Option?>(null)
    val option: StateFlow<Option?> = _option.asStateFlow()

    fun loadOptionsForQuestion(surveyId: String, questionId: String) {
        viewModelScope.launch {
            repository.getOptionsForQuestion(surveyId, questionId).collect { options ->
                _options.value = options
            }
        }
    }

    fun loadOption(surveyId: String, questionId: String, optionId: String) {
        viewModelScope.launch {
            repository.getOption(surveyId, questionId, optionId).collect { option ->
                _option.value = option
            }
        }
    }

    fun addOption(surveyId: String, questionId: String, option: Option) {
        repository.addOptionToQuestion(surveyId, questionId, option)
    }

    fun updateOption(surveyId: String, questionId: String, option: Option) {
        repository.updateOptionInQuestion(surveyId, questionId, option)
    }

    fun deleteOption(surveyId: String, questionId: String, optionId: String) {
        repository.deleteOptionFromQuestion(surveyId, questionId, optionId)
    }
}
