package com.theseuntaylor.picsomeapp.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi
import com.theseuntaylor.picsomeapp.lib_home.domain.model.PhotoImageUrls

/** Provided by MainActivity; the default (used in previews) can't resize, so full-size URLs are used. */
val LocalPhotoImageUrls = staticCompositionLocalOf { PhotoImageUrls { _, _, _, _ -> null } }

/** URL for displaying this photo about [targetWidthPx] wide; falls back to the full-size original. */
@Composable
fun PhotoUi.sizedUrl(targetWidthPx: Int): String =
    LocalPhotoImageUrls.current.sized(id, width, height, targetWidthPx) ?: download_url
