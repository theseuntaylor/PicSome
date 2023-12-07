package com.theseuntaylor.picsomeapp.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.theseuntaylor.picsomeapp.R
import com.theseuntaylor.picsomeapp.core.theme.Typography
import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi

@Composable
fun PhotoItem(
    modifier: Modifier = Modifier,
    photo: PhotoUi,
    toggleFavourites: (id: String, isFavourite: Boolean) -> Unit,
    navigateToFullScreen: (PhotoUi) -> Unit,
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable(
                // navigate to screen that shows image in full screen
                onClick = { navigateToFullScreen(photo) }
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                AsyncImage(
                    model = photo.download_url,
                    contentDescription = "Cover image for ${photo.id}",
                    onLoading = {},
                    placeholder = painterResource(id = R.drawable.ic_image_24)
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
                            tint = if (photo.isFavourite) Color.Red else Color.Gray,
                            contentDescription = stringResource(id = R.string.favourites_icon_content_description)
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