package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.Like
import com.example.ratemate.repository.LikeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LikeViewModelFactory(private val repository: LikeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LikeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class LikeViewModel(private val repository: LikeRepository) : ViewModel() {

    // 모든 좋아요 항목 목록을 위한 StateFlow
    private val _likes = MutableStateFlow<List<Like>>(emptyList())
    val likes: StateFlow<List<Like>> = _likes.asStateFlow()

    // 특정 좋아요 항목을 위한 StateFlow
    private val _like = MutableStateFlow<Like?>(null)
    val like: StateFlow<Like?> = _like.asStateFlow()

    init {
        // 모든 좋아요 항목 로드
        loadAllLikes()
    }

    private fun loadAllLikes() {
        viewModelScope.launch {
            repository.getAllLikes().collect { likes ->
                _likes.value = likes
            }
        }
    }

    fun getLike(likeId: String) {
        viewModelScope.launch {
            repository.getLike(likeId).collect { like ->
                _like.value = like
            }
        }
    }

    fun setLike(likeId: String, like: Like) {
        repository.setLike(likeId, like)
    }

    fun deleteLike(likeId: String) {
        repository.deleteLike(likeId)
    }
}
