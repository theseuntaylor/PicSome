package com.application.storelabs.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.application.storelabs.R
import com.application.storelabs.core.theme.Typography
import com.application.storelabs.lib_home.data.model.PhotoDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoItem(photo: PhotoDTO, modifier: Modifier = Modifier) {
    Card(
        onClick = { },
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = photo.download_url,
                contentDescription = "Cover image for ${photo.id}",
                onLoading = {},
                placeholder = painterResource(id = R.drawable.ic_image_24)
            )
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