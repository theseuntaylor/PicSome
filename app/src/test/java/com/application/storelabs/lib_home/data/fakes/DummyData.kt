package com.application.storelabs.lib_home.data.fakes

import com.application.storelabs.lib_home.domain.model.Photo
import com.application.storelabs.lib_home.local.PhotoEntity

object DummyData {
    val cachedFavouritePhotos = listOf(
        PhotoEntity(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = true,
        ),
        PhotoEntity(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = false,
        ),
    )
    val favouritePhotos = listOf(
        Photo(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = true,
        ),
        Photo(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = false,
        ),
    )
}