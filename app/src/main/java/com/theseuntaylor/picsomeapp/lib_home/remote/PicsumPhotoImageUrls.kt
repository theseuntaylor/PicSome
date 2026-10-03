package com.theseuntaylor.picsomeapp.lib_home.remote

import com.theseuntaylor.picsomeapp.lib_home.domain.model.PhotoImageUrls
import com.theseuntaylor.picsomeapp.utils.BASE_URL
import javax.inject.Inject

/** Picsum resizes on the fly at /id/{id}/{width}/{height}; never upscale past the original. */
class PicsumPhotoImageUrls @Inject constructor() : PhotoImageUrls {
    override fun sized(id: String, width: Int, height: Int, targetWidthPx: Int): String? {
        if (width <= 0 || height <= 0 || targetWidthPx <= 0) return null
        val sizedWidth = targetWidthPx.coerceAtMost(width)
        val sizedHeight = (sizedWidth.toLong() * height / width).toInt().coerceAtLeast(1)
        return "${BASE_URL}id/$id/$sizedWidth/$sizedHeight"
    }
}
