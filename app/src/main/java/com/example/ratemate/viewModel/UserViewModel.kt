package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.User
import com.example.ratemate.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class UserViewModel(private val repository: UserRepository) : ViewModel() {

    // 모든 사용자 목록을 위한 StateFlow
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    // 특정 사용자를 위한 StateFlow
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        // 모든 사용자 로드
        loadAllUsers()
    }

    private fun loadAllUsers() {
        viewModelScope.launch {
            repository.getAllUsers().collect {
                _users.value = it
            }
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            repository.getUser(userId).collect {
                _user.value = it
            }
        }
    }

    fun addUser(user: User) {
        repository.addUser(user)
    }

    fun deleteUser(userId: String) {
        repository.deleteUser(userId)
    }

    fun updateUser(userId: String, updatedFields: Map<String, Any>) {
        repository.updateUser(userId, updatedFields)
    }

    fun addSurveyToCreated(userId: String, surveyId: String) {
        repository.addSurveyToCreated(userId, surveyId)
    }

    fun addSurveyToParticipated(userId: String, surveyId: String) {
        repository.addSurveyToParticipated(userId, surveyId)
    }
}
