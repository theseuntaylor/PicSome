package com.application.storelabs.lib_home.domain.model

import com.application.storelabs.lib_home.local.PhotoEntity

data class Photo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String,
    var isFavourite: Boolean = false
)

fun Photo.asEntity() = PhotoEntity(
    id = id,
    author = author,
    width = width,
    height = height,
    url = url,
    download_url = download_url,
    isFavourite = false
)