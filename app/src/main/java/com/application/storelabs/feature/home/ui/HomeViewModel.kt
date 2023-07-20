package com.application.storelabs.feature.home.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.storelabs.lib_home.data.PhotosRepository
import com.application.storelabs.lib_home.data.model.PhotoDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val photos: List<PhotoDTO> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PhotosRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    private val _photosStream = MutableSharedFlow<List<PhotoDTO>>()
    val photosStream = _photosStream.asSharedFlow()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getPhotos() = viewModelScope.launch {
        repository.getPhotos().collect { photoResponse ->
            _uiState.update {
                it.copy(photos = photoResponse.shuffled())
            }
        }
    }

}