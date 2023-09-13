package com.theseuntaylor.picsomeapp.feature.home.model

import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo

data class PhotoUi(
    val id: String,
    val author: String,
    val download_url: String,
    var isFavourite: Boolean
)

fun Photo.asUiModel() = PhotoUi(
    id = id,
    author = author,
    download_url = download_url,
    isFavourite = isFavourite
)