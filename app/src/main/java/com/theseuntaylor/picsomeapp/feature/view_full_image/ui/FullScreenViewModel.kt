package com.theseuntaylor.picsomeapp.feature.view_full_image.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FullScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // var urlToDownload: String = checkNotNull(savedStateHandle["urlToDownload"])

}