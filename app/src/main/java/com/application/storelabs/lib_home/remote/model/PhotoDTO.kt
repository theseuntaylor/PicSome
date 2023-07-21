package com.application.storelabs.lib_home.remote.model

import com.application.storelabs.lib_home.domain.model.Photo

data class PhotoDTO(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String,
)

fun PhotoDTO.toDomainModel() = Photo(
    id = id,
    author = author,
    width = width,
    height = height,
    url = url,
    download_url = download_url,
    isFavourite = false
)