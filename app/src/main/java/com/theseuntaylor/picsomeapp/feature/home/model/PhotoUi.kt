package com.theseuntaylor.picsomeapp.feature.home.model

import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo

data class PhotoUi(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val download_url: String,
    var isFavourite: Boolean
)

private val PhotoUi.hasDimensions get() = width > 0 && height > 0

/** Width / height of the original photo, used to reserve the right space before it loads. */
val PhotoUi.aspectRatio: Float
    get() = if (hasDimensions) width.toFloat() / height else 1f

fun Photo.asUiModel() = PhotoUi(
    id = id,
    author = author,
    width = width,
    height = height,
    download_url = download_url,
    isFavourite = isFavourite
)
