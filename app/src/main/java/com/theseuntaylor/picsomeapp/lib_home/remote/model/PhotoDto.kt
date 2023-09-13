package com.theseuntaylor.picsomeapp.lib_home.remote.model

import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo

data class PhotoDto(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String,
)

fun PhotoDto.toDomainModel() = Photo(
    id = id,
    author = author,
    width = width,
    height = height,
    url = url,
    download_url = download_url,
    isFavourite = false
)