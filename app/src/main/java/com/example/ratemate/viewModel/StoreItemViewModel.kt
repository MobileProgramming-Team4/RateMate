package com.example.ratemate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ratemate.data.StoreItem
import com.example.ratemate.repository.StoreItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StoreItemViewModelFactory(private val repository: StoreItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoreItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class StoreItemViewModel(private val repository: StoreItemRepository) : ViewModel() {

    // 모든 상점 아이템 목록을 위한 StateFlow
    private val _storeItems = MutableStateFlow<List<StoreItem>>(emptyList())
    val storeItems: StateFlow<List<StoreItem>> = _storeItems.asStateFlow()

    // 특정 상점 아이템을 위한 StateFlow
    private val _storeItem = MutableStateFlow<StoreItem?>(null)
    val storeItem: StateFlow<StoreItem?> = _storeItem.asStateFlow()

    init {
        // 모든 상점 아이템 로드
        loadAllStoreItems()
    }

    private fun loadAllStoreItems() {
        viewModelScope.launch {
            repository.getAllItems().collect { items ->
                _storeItems.value = items
            }
        }
    }

    fun getStoreItem(itemId: String) {
        viewModelScope.launch {
            repository.getItem(itemId).collect { item ->
                _storeItem.value = item
            }
        }
    }

    fun addStoreItem(storeItem: StoreItem) {
        repository.addItem(storeItem)
    }

    fun deleteStoreItem(itemId: String) {
        repository.deleteItem(itemId)
    }

    fun updateStoreItem(itemId: String, updatedFields: Map<String, Any>) {
        repository.updateItem(itemId, updatedFields)
    }
}
