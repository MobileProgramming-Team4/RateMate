package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.Comment
import com.example.ratemate.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CommentViewModelFactory(private val repository: CommentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class CommentViewModel(private val repository: CommentRepository) : ViewModel() {

    // 모든 댓글 목록을 위한 StateFlow
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    // 특정 댓글을 위한 StateFlow
    private val _comment = MutableStateFlow<Comment?>(null)
    val comment: StateFlow<Comment?> = _comment.asStateFlow()

    init {
        // 모든 댓글 로드
        loadAllComments()
    }

    private fun loadAllComments() {
        viewModelScope.launch {
            repository.getAllComments().collect { comments ->
                _comments.value = comments
            }
        }
    }

    fun getComment(commentId: String) {
        viewModelScope.launch {
            repository.getComment(commentId).collect { comment ->
                _comment.value = comment
            }
        }
    }

    fun addComment(comment: Comment) {
        repository.addComment(comment)
    }

    fun deleteComment(commentId: String) {
        repository.deleteComment(commentId)
    }

    fun updateComment(commentId: String, updatedFields: Map<String, Any>) {
        repository.updateComment(commentId, updatedFields)
    }
}