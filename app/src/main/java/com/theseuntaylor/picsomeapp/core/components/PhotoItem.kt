package com.theseuntaylor.picsomeapp.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.theseuntaylor.picsomeapp.R
import com.theseuntaylor.picsomeapp.core.theme.Typography
import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi
import com.theseuntaylor.picsomeapp.feature.home.model.aspectRatio
import com.theseuntaylor.picsomeapp.feature.home.model.sizedUrl

@Composable
fun PhotoItem(
    modifier: Modifier = Modifier,
    photo: PhotoUi,
    toggleFavourites: (id: String, isFavourite: Boolean) -> Unit,
    onPhotoClicked: (id: String) -> Unit,
) {
    Card(
        modifier = modifier.padding(8.dp).clickable { onPhotoClicked(photo.id) }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxWithConstraints {
                AsyncImage(
                    model = photo.sizedUrl(targetWidthPx = constraints.maxWidth),
                    contentDescription = "Cover image for ${photo.id}",
                    placeholder = ColorPainter(colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(photo.aspectRatio)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    IconButton(modifier = Modifier, onClick = {
                        toggleFavourites(photo.id, !photo.isFavourite)
                    }) {
                        Icon(
                            painter = if (photo.isFavourite) painterResource(id = R.drawable.ic_favorite_24)
                            else painterResource(id = R.drawable.ic_favorite_border_24),
                            tint = if (photo.isFavourite) colorScheme.primary else Color.Gray,
                            contentDescription = stringResource(
                                id = if (photo.isFavourite) R.string.remove_from_favourites
                                else R.string.add_to_favourites
                            )
                        )
                    }
                }
            }
            Text(
                "by: ${photo.author}",
                modifier = Modifier.padding(5.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,

                style = Typography.bodyLarge
            )
        }
    }
}