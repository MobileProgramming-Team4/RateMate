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

    init {
        // 모든 옵션 로드
        loadAllOptions()
    }

    private fun loadAllOptions() {
        viewModelScope.launch {
            repository.getAllOptions().collect { options ->
                _options.value = options
            }
        }
    }

    fun getOption(optionId: String) {
        viewModelScope.launch {
            repository.getOption(optionId).collect { option ->
                _option.value = option
            }
        }
    }

    fun setOption(optionId: String, option: Option) {
        repository.setOption(optionId, option)
    }

    fun deleteOption(optionId: String) {
        repository.deleteOption(optionId)
    }
}
