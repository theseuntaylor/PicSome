package com.theseuntaylor.picsomeapp.feature.home.model

import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo
import com.theseuntaylor.picsomeapp.utils.BASE_URL

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

/**
 * A copy of the photo resized by Picsum to [targetWidthPx] wide (never upscaled), keeping its
 * aspect ratio. Use for display; [PhotoUi.download_url] is the full-size original.
 */
fun PhotoUi.sizedUrl(targetWidthPx: Int): String {
    if (!hasDimensions || targetWidthPx <= 0) return download_url
    val sizedWidth = targetWidthPx.coerceAtMost(width)
    val sizedHeight = (sizedWidth.toLong() * height / width).toInt().coerceAtLeast(1)
    return "${BASE_URL}id/$id/$sizedWidth/$sizedHeight"
}

fun Photo.asUiModel() = PhotoUi(
    id = id,
    author = author,
    width = width,
    height = height,
    download_url = download_url,
    isFavourite = isFavourite
)
