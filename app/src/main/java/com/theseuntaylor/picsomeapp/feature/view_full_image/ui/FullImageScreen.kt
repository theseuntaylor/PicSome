package com.theseuntaylor.picsomeapp.feature.view_full_image.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.theseuntaylor.picsomeapp.R
import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi

// all i need for this screen is the url to the image.
@Composable
fun FullImageScreen(
    snackBarHostState: SnackbarHostState,
    photo: PhotoUi = PhotoUi(
        id = "",
        author = "",
        download_url = "",
        isFavourite = true,
    ),
) {
    // app bar that shows the creator's name and back button of course.

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Green)
                .weight(9f)
        ) {
            AsyncImage(
                model = photo.download_url,
                contentDescription = "Cover image for ${photo.id}",
                onLoading = {},
                placeholder = painterResource(id = R.drawable.ic_image_24)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // remove favourites, maybe?
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = Color.Red)
            ) {}
            //share
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = Color.Gray)
            ) {}
            //download
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = Color.Blue)
            ) {}

        }
    }
}