package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.Response
import com.example.ratemate.repository.ResponseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ResponseViewModelFactory(private val repository: ResponseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResponseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResponseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class ResponseViewModel(private val repository: ResponseRepository) : ViewModel() {

    // 모든 응답 목록을 위한 StateFlow
    private val _responses = MutableStateFlow<List<Response>>(emptyList())
    val responses: StateFlow<List<Response>> = _responses.asStateFlow()

    // 특정 응답을 위한 StateFlow
    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response.asStateFlow()

    init {
        // 모든 응답 로드
        loadAllResponses()
    }

    private fun loadAllResponses() {
        viewModelScope.launch {
            repository.getAllResponses().collect { responses ->
                _responses.value = responses
            }
        }
    }

    fun getResponse(responseId: String) {
        viewModelScope.launch {
            repository.getResponse(responseId).collect { response ->
                _response.value = response
            }
        }
    }

    fun addResponse(response: Response) {
        repository.addResponse(response)
    }

    fun deleteResponse(responseId: String) {
        repository.deleteResponse(responseId)
    }

    fun updateResponse(responseId: String, updatedFields: Map<String, Any>) {
        repository.updateResponse(responseId, updatedFields)
    }
}
