package com.example.ratemate.viewmodel

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
    private val _responses = MutableStateFlow<List<Response>>(emptyList())
    val responses: StateFlow<List<Response>> = _responses.asStateFlow()

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response.asStateFlow()

    fun addResponse(response: Response) {
        viewModelScope.launch {
            repository.addResponse(response)
        }
    }

    fun deleteResponse(userId: String) {
        viewModelScope.launch {
            repository.deleteResponse(userId)
        }
    }

    fun updateResponse(userId: String, updatedFields: Map<String, Any>) {
        viewModelScope.launch {
            repository.updateResponse(userId, updatedFields)
        }
    }

    fun fetchAllResponses() {
        viewModelScope.launch {
            repository.getAllResponses().collect { listOfResponses ->
                _responses.value = listOfResponses
            }
        }
    }

    fun fetchResponse(userId: String) {
        viewModelScope.launch {
            repository.getResponse(userId).collect { response ->
                _response.value = response
            }
        }
    }
}
