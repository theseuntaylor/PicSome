package com.theseuntaylor.picsomeapp.lib_home.domain.model

/** Builds display URLs for a photo; implemented by whichever image source backs the app. */
fun interface PhotoImageUrls {
    /**
     * A copy of the photo about [targetWidthPx] wide, keeping its aspect ratio, or null if the
     * source can't resize (callers then fall back to the full-size download URL).
     */
    fun sized(id: String, width: Int, height: Int, targetWidthPx: Int): String?
}
