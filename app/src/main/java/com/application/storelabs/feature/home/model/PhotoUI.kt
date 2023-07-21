package com.application.storelabs.feature.home.model

import com.application.storelabs.lib_home.domain.model.Photo

data class PhotoUI(
    val id: String,
    val author: String,
    val download_url: String,
    var isFavourite: Boolean
)

fun Photo.asUiModel() = PhotoUI(
    id = id,
    author = author,
    download_url = download_url,
    isFavourite = isFavourite
)