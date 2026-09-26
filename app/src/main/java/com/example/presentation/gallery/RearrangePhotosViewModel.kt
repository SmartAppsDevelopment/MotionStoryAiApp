package com.example.presentation.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.PhotoItem
import com.example.data.repository.FakePhotoRepository
import com.example.data.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RearrangePhotosViewModel(
    private val photoRepository: PhotoRepository = FakePhotoRepository()
) : ViewModel() {

    private val _photos = MutableStateFlow<List<PhotoItem>>(emptyList())
    val photos = _photos.asStateFlow()

    init {
        viewModelScope.launch {
            photoRepository.getSelectedPhotos().collect { items ->
                _photos.value = items
            }
        }
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        if (fromIndex !in _photos.value.indices || toIndex !in _photos.value.indices) return
        val list = _photos.value.toMutableList()
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        _photos.value = list.mapIndexed { index, photoItem -> photoItem.copy(order = index + 1) }
    }

    fun saveReorder(onComplete: () -> Unit) {
        viewModelScope.launch {
            photoRepository.reorderSelectedPhotos(_photos.value)
            onComplete()
        }
    }
}
