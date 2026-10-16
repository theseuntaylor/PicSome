package com.theseuntaylor.picsomeapp.core.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.theseuntaylor.picsomeapp.R
import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi
import com.theseuntaylor.picsomeapp.feature.home.model.aspectRatio

/** Spacing between grid cards and around the grid; use with [photoGridContentPadding]. */
val PhotoGridSpacing = 8.dp
val photoGridContentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
val photoGridArrangement = Arrangement.spacedBy(PhotoGridSpacing)

/**
 * Image-first photo card: the photo fills the card, the photographer is named on a gradient scrim
 * along the bottom edge, and the favourite toggle floats in the top corner.
 */
@Composable
fun PhotoItem(
    modifier: Modifier = Modifier,
    photo: PhotoUi,
    toggleFavourites: (id: String, isFavourite: Boolean) -> Unit,
    onPhotoClicked: (id: String) -> Unit,
) {
    Card(
        onClick = { onPhotoClicked(photo.id) },
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceContainerHigh),
    ) {
        BoxWithConstraints {
            val imageUrl = photo.sizedUrl(targetWidthPx = constraints.maxWidth)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Cover image for ${photo.id}",
                placeholder = ColorPainter(colorScheme.surfaceContainerHigh),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(photo.aspectRatio)
            )
            Text(
                text = photo.author,
                color = Color.White,
                // Shadow is a backstop for very bright photos and large font scales, where the
                // name can extend into the lighter top of the scrim.
                style = MaterialTheme.typography.labelLarge.copy(shadow = TextShadow),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    // Fixed dark scrim (not a theme colour) so white text reads on any photo.
                    .background(Brush.verticalGradient(*ScrimStops))
                    .padding(start = 12.dp, end = 12.dp, top = 28.dp, bottom = 10.dp)
            )
            FavouriteToggle(
                isFavourite = photo.isFavourite,
                onToggle = { toggleFavourites(photo.id, it) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun FavouriteToggle(
    isFavourite: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    val iconScale by animateFloatAsState(
        targetValue = if (isFavourite) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
        label = "favouriteIconScale",
    )
    FilledIconToggleButton(
        checked = isFavourite,
        onCheckedChange = { checked ->
            haptics.performHapticFeedback(
                if (checked) HapticFeedbackType.ToggleOn else HapticFeedbackType.ToggleOff
            )
            onToggle(checked)
        },
        colors = IconButtonDefaults.filledIconToggleButtonColors(
            // Translucent surface keeps the unfilled heart visible on light and dark photos alike.
            containerColor = colorScheme.surface.copy(alpha = 0.72f),
            contentColor = colorScheme.onSurface,
            checkedContainerColor = colorScheme.primary,
            checkedContentColor = colorScheme.onPrimary,
        ),
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(
                if (isFavourite) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24
            ),
            contentDescription = stringResource(
                if (isFavourite) R.string.remove_from_favourites else R.string.add_to_favourites
            ),
            modifier = Modifier
                .size(20.dp)
                .scale(iconScale)
        )
    }
}

// Reaches most of its strength early so the first line of a wrapped name is already on dark.
private val ScrimStops = arrayOf(
    0f to Color.Transparent,
    0.35f to Color.Black.copy(alpha = 0.45f),
    1f to Color.Black.copy(alpha = 0.7f),
)
private val TextShadow = Shadow(color = Color.Black.copy(alpha = 0.6f), offset = Offset(0f, 1f), blurRadius = 6f)
