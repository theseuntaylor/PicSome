package com.theseuntaylor.picsomeapp.feature.home.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theseuntaylor.picsomeapp.feature.home.model.HomeUiState
import com.theseuntaylor.picsomeapp.feature.home.model.asUiModel
import com.theseuntaylor.picsomeapp.lib_home.data.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PhotosRepository,
) : ViewModel() {

    private val _uiState = mutableStateOf<HomeUiState>(HomeUiState.Initial)
    val uiState: State<HomeUiState> = _uiState

    init {
        getPhotos()
    }

    // withContext(dispatcher) { suspend block }
    fun getPhotos() = viewModelScope.launch {
        repository.getPhotos()
            .onStart { _uiState.value = HomeUiState.Loading }
            .catch {
                _uiState.value =
                    HomeUiState.Error(it.message ?: "There was an error loading images")
            }

            .map { photos ->
                photos.map { it.asUiModel() }
            }
            .collect { photosUI ->
                _uiState.value = HomeUiState.Success(photosUI)
            }
    }

    fun loadMorePhotos() {
        val current = _uiState.value as? HomeUiState.Success ?: return
        if (current.isLoadingMore || current.endReached) return
        _uiState.value = current.copy(isLoadingMore = true, loadMoreError = null)
        viewModelScope.launch {
            val result = try {
                val added = repository.loadNextPage()
                val photos = repository.getPhotos().first().map { it.asUiModel() }
                Result.success(photos to !added)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Result.failure(e)
            }
            // Re-read state: a favourite may have been toggled while the page was loading.
            val latest = _uiState.value as? HomeUiState.Success ?: return@launch
            _uiState.value = result.fold(
                onSuccess = { (photos, endReached) ->
                    latest.copy(data = photos, isLoadingMore = false, endReached = endReached)
                },
                onFailure = {
                    latest.copy(
                        isLoadingMore = false,
                        loadMoreError = it.message ?: "There was an error loading more images"
                    )
                }
            )
        }
    }

    fun toggleFavourite(id: String, isFavourite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavourite(id, isFavourite)
            repository.getPhotos()
                .map { photos ->
                    photos.map { it.asUiModel() }
                }
                .collect { photosUI ->
                    val current = _uiState.value
                    _uiState.value = if (current is HomeUiState.Success) {
                        current.copy(data = photosUI)
                    } else {
                        HomeUiState.Success(photosUI)
                    }
                }
        }

    }
}